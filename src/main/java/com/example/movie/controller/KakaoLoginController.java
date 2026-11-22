package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import com.example.movie.dto.LoginDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 카카오 로그인(REST API 직접 연동).
 * 로그인 성공 후 세션 구성은 LoginController.login_login과 같게 맞춘다.
 * 다른 화면들이 session "user"(UserDetails)와 "roles"를 읽기 때문이다.
 */
@Slf4j
@Controller
@RequestMapping("/movie/login/kakao")
public class KakaoLoginController {

    private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String STATE_KEY = "kakaoOAuthState";
    private static final String USERNAME_PREFIX = "kakao_";
    private static final int NICKNAME_MAX = 100; // members.nickname VARCHAR(100)

    private final LoginService loginService;
    private final UserDetailsService userDetailsService;
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public KakaoLoginController(@Qualifier("loginService") LoginService loginService,
                                UserDetailsService userDetailsService,
                                @Value("${spring.login.KAKAO_CLIENT_ID:}") String clientId,
                                @Value("${spring.login.KAKAO_CLIENT_SECRET:}") String clientSecret,
                                @Value("${KAKAO_REDIRECT_URI:http://localhost:8080/movie/login/kakao/callback}") String redirectUri) {
        this.loginService = loginService;
        this.userDetailsService = userDetailsService;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    @GetMapping
    public String authorize(HttpSession session, RedirectAttributes ra) {
        if (!StringUtils.hasText(clientId)) {
            log.warn("KAKAO_CLIENT_ID 환경변수가 없어 카카오 로그인을 시작할 수 없습니다.");
            ra.addFlashAttribute("msg", "카카오 로그인이 아직 설정되지 않았습니다.");
            return "redirect:/movie/login/login";
        }

        // 콜백이 우리가 시작한 요청인지 확인하기 위한 값(CSRF 방지)
        String state = UUID.randomUUID().toString();
        session.setAttribute(STATE_KEY, state);

        String url = UriComponentsBuilder.fromHttpUrl(AUTHORIZE_URL)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .encode()
                .toUriString();
        return "redirect:" + url;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam(value = "error", required = false) String error,
                           HttpServletRequest request,
                           RedirectAttributes ra) {
        HttpSession session = request.getSession();
        String savedState = (String) session.getAttribute(STATE_KEY);
        session.removeAttribute(STATE_KEY);

        if (error != null || !StringUtils.hasText(code) || savedState == null || !savedState.equals(state)) {
            // error 값은 외부 입력이라 그대로 로그에 남기지 않는다(로그 줄 위조 방지).
            log.warn("카카오 로그인 콜백 거부: errorPresent={}, codePresent={}, stateMatched={}",
                    error != null, StringUtils.hasText(code), savedState != null && savedState.equals(state));
            return failRedirect(ra);
        }

        try {
            String accessToken = requestAccessToken(code);
            JsonNode kakaoUser = requestUserInfo(accessToken);

            LoginDTO member = findOrJoin(kakaoUser);
            signIn(request, member);

            ra.addFlashAttribute("msg", "정상적으로 로그인되었습니다");
            return "redirect:/movie/mains";
        } catch (RestClientResponseException e) {
            // 응답 본문은 남기지 않고 상태 코드만 남긴다.
            log.warn("카카오 로그인 실패: 카카오 응답 {}", e.getRawStatusCode());
            return failRedirect(ra);
        } catch (RestClientException | IllegalStateException e) {
            log.warn("카카오 로그인 실패: {}", e.getClass().getSimpleName());
            return failRedirect(ra);
        } catch (DataAccessException | AuthenticationException e) {
            log.error("카카오 로그인 중 회원 처리 실패", e);
            return failRedirect(ra);
        }
    }

    private String requestAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);
        if (StringUtils.hasText(clientSecret)) {
            form.add("client_secret", clientSecret);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JsonNode body = restTemplate.postForObject(TOKEN_URL, new HttpEntity<>(form, headers), JsonNode.class);
        if (body == null || !body.hasNonNull("access_token")) {
            throw new IllegalStateException("토큰 응답에 access_token이 없습니다.");
        }
        return body.get("access_token").asText();
    }

    private JsonNode requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode body = restTemplate.exchange(USER_URL, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class).getBody();
        if (body == null || !body.hasNonNull("id")) {
            throw new IllegalStateException("사용자 정보 응답에 id가 없습니다.");
        }
        return body;
    }

    /** 카카오 회원번호로 members를 찾고, 없으면 새로 가입시킨다. */
    private LoginDTO findOrJoin(JsonNode kakaoUser) {
        String username = USERNAME_PREFIX + kakaoUser.get("id").asText();

        LoginDTO member = loginService.login(username);
        if (member != null) {
            return member;
        }

        // 일반 로그인 폼으로는 들어올 수 없도록 아무도 모르는 무작위 비밀번호를 넣는다(Login_join에서 BCrypt로 암호화).
        LoginDTO newMember = LoginDTO.builder()
                .username(username)
                .nickname(extractNickname(kakaoUser))
                .pw(UUID.randomUUID().toString())
                .build();
        try {
            loginService.Login_join(newMember);
        } catch (DuplicateKeyException e) {
            // 같은 카카오 계정의 콜백이 동시에 들어와 이미 가입된 경우: 다시 조회해서 쓴다.
            log.info("카카오 회원이 이미 가입되어 있어 기존 계정을 사용합니다: {}", username);
        }

        member = loginService.login(username);
        if (member == null) {
            throw new IllegalStateException("카카오 회원 가입 후 조회에 실패했습니다: " + username);
        }
        return member;
    }

    private String extractNickname(JsonNode kakaoUser) {
        String nickname = kakaoUser.path("kakao_account").path("profile").path("nickname").asText("");
        if (nickname.isEmpty()) {
            nickname = kakaoUser.path("properties").path("nickname").asText("");
        }
        if (nickname.isEmpty()) {
            return null;
        }
        return nickname.length() > NICKNAME_MAX ? nickname.substring(0, NICKNAME_MAX) : nickname;
    }

    /** LoginController.login_login과 같은 세션 구성. 비밀번호 검증 대신 카카오 인증을 거쳤으므로 AuthenticationManager는 쓰지 않는다. */
    private void signIn(HttpServletRequest request, LoginDTO member) {
        // 로그인 전 세션 ID를 그대로 쓰지 않도록 바꾼다(세션 고정 공격 방지).
        request.changeSessionId();
        HttpSession session = request.getSession();

        UserDetails userDetails = userDetailsService.loadUserByUsername(member.getUsername());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        session.setAttribute("user", userDetails);
        session.setAttribute("roles", member.getRoles());
        session.setMaxInactiveInterval(2000);
    }

    private String failRedirect(RedirectAttributes ra) {
        ra.addFlashAttribute("msg", "카카오 로그인에 실패했습니다.");
        return "redirect:/movie/login/login";
    }
}
