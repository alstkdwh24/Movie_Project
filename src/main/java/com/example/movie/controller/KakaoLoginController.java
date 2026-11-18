package com.example.movie.controller;

import com.example.movie.LoginService.SocialLoginService;
import com.example.movie.dto.LoginDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 카카오 로그인(REST API 직접 연동). 회원 연결과 세션 구성은 SocialLoginService가 맡는다.
 */
@Slf4j
@Controller
@RequestMapping("/movie/login/kakao")
public class KakaoLoginController {

    private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String STATE_KEY = "kakaoOAuthState";

    private final SocialLoginService socialLoginService;
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public KakaoLoginController(SocialLoginService socialLoginService,
                                @Value("${KAKAO_CLIENT_ID:}") String clientId,
                                @Value("${KAKAO_CLIENT_SECRET:}") String clientSecret,
                                @Value("${KAKAO_REDIRECT_URI:http://localhost:8080/movie/login/kakao/callback}") String redirectUri) {
        this.socialLoginService = socialLoginService;
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

            LoginDTO member = socialLoginService.findOrJoin(
                    SocialLoginService.KAKAO_PREFIX, kakaoUser.get("id").asText(), extractNickname(kakaoUser));
            socialLoginService.signIn(request, member);

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

    private String extractNickname(JsonNode kakaoUser) {
        String nickname = kakaoUser.path("kakao_account").path("profile").path("nickname").asText("");
        if (nickname.isEmpty()) {
            nickname = kakaoUser.path("properties").path("nickname").asText("");
        }
        return nickname;
    }

    private String failRedirect(RedirectAttributes ra) {
        ra.addFlashAttribute("msg", "카카오 로그인에 실패했습니다.");
        return "redirect:/movie/login/login";
    }
}
