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
 * 네이버 로그인(REST API 직접 연동). 회원 연결과 세션 구성은 SocialLoginService가 맡는다.
 */
@Slf4j
@Controller
@RequestMapping("/movie/login/naver")
public class NaverLoginController {

    private static final String AUTHORIZE_URL = "https://nid.naver.com/oauth2.0/authorize";
    private static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    private static final String USER_URL = "https://openapi.naver.com/v1/nid/me";
    private static final String STATE_KEY = "naverOAuthState";

    private final SocialLoginService socialLoginService;
    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public NaverLoginController(SocialLoginService socialLoginService,
                                @Value("${spring.login.NAVER_CLIENT_ID:}") String clientId,
                                @Value("${spring.login.NAVER_CLIENT_SECRET:}") String clientSecret,
                                @Value("${NAVER_REDIRECT_URI:http://localhost:8080/movie/login/naver/callback}") String redirectUri) {
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
        // 네이버는 토큰 요청에 client secret이 꼭 필요하다.
        if (!StringUtils.hasText(clientId) || !StringUtils.hasText(clientSecret)) {
            log.warn("spring.login.NAVER_CLIENT_ID 또는 NAVER_CLIENT_SECRET 설정이 없어 네이버 로그인을 시작할 수 없습니다.");
            ra.addFlashAttribute("msg", "네이버 로그인이 아직 설정되지 않았습니다.");
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
            log.warn("네이버 로그인 콜백 거부: errorPresent={}, codePresent={}, stateMatched={}",
                    error != null, StringUtils.hasText(code), savedState != null && savedState.equals(state));
            return failRedirect(ra);
        }

        try {
            String accessToken = requestAccessToken(code, state);
            JsonNode profile = requestProfile(accessToken);

            LoginDTO member = socialLoginService.findOrJoin(
                    SocialLoginService.NAVER_PREFIX, profile.get("id").asText(), profile.path("nickname").asText(""));
            socialLoginService.signIn(request, member);

            ra.addFlashAttribute("msg", "정상적으로 로그인되었습니다");
            return "redirect:/movie/mains";
        } catch (RestClientResponseException e) {
            // 응답 본문은 남기지 않고 상태 코드만 남긴다.
            log.warn("네이버 로그인 실패: 네이버 응답 {}", e.getRawStatusCode());
            return failRedirect(ra);
        } catch (RestClientException | IllegalStateException e) {
            log.warn("네이버 로그인 실패: {}", e.getClass().getSimpleName());
            return failRedirect(ra);
        } catch (DataAccessException | AuthenticationException e) {
            log.error("네이버 로그인 중 회원 처리 실패", e);
            return failRedirect(ra);
        }
    }

    private String requestAccessToken(String code, String state) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", code);
        form.add("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 네이버는 실패해도 200으로 error 필드를 돌려주므로 access_token 유무로 판단한다.
        JsonNode body = restTemplate.postForObject(TOKEN_URL, new HttpEntity<>(form, headers), JsonNode.class);
        if (body == null || !body.hasNonNull("access_token")) {
            throw new IllegalStateException("토큰 응답에 access_token이 없습니다.");
        }
        return body.get("access_token").asText();
    }

    /** 응답 형식: {"resultcode":"00","message":"success","response":{"id":"...","nickname":"..."}} */
    private JsonNode requestProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        JsonNode body = restTemplate.exchange(USER_URL, HttpMethod.GET, new HttpEntity<>(headers), JsonNode.class).getBody();
        if (body == null || !"00".equals(body.path("resultcode").asText())
                || !body.path("response").hasNonNull("id")) {
            throw new IllegalStateException("사용자 정보 응답이 올바르지 않습니다.");
        }
        return body.get("response");
    }

    private String failRedirect(RedirectAttributes ra) {
        ra.addFlashAttribute("msg", "네이버 로그인에 실패했습니다.");
        return "redirect:/movie/login/login";
    }
}
