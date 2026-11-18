package com.example.movie.LoginService;

import com.example.movie.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.UUID;

/**
 * 카카오·네이버 로그인이 함께 쓰는 회원 연결과 세션 구성.
 * 세션 구성은 LoginController.login_login과 같게 맞춘다(다른 화면들이 session "user"·"roles"를 읽음).
 */
@Slf4j
@Service
public class SocialLoginService {

    public static final String KAKAO_PREFIX = "kakao_";
    public static final String NAVER_PREFIX = "naver_";
    private static final int NICKNAME_MAX = 100; // members.nickname VARCHAR(100)

    private final LoginService loginService;
    private final UserDetailsService userDetailsService;

    public SocialLoginService(@Qualifier("loginService") LoginService loginService,
                              UserDetailsService userDetailsService) {
        this.loginService = loginService;
        this.userDetailsService = userDetailsService;
    }

    /** 일반 가입에서 쓸 수 없는 아이디인지(소셜 로그인 전용 접두사). 대소문자는 무시한다. */
    public static boolean isReservedUsername(String username) {
        if (username == null) {
            return true;
        }
        String lower = username.toLowerCase(Locale.ROOT);
        return lower.startsWith(KAKAO_PREFIX) || lower.startsWith(NAVER_PREFIX);
    }

    /** 접두사+소셜 회원번호로 members를 찾고, 없으면 새로 가입시킨다. */
    public LoginDTO findOrJoin(String prefix, String socialId, String nickname) {
        String username = prefix + socialId;

        LoginDTO member = loginService.login(username);
        if (member != null) {
            return member;
        }

        // 일반 로그인 폼으로는 들어올 수 없도록 아무도 모르는 무작위 비밀번호를 넣는다(Login_join에서 BCrypt로 암호화).
        LoginDTO newMember = LoginDTO.builder()
                .username(username)
                .nickname(trimNickname(nickname))
                .pw(UUID.randomUUID().toString())
                .build();
        try {
            loginService.Login_join(newMember);
        } catch (DuplicateKeyException e) {
            // 같은 계정의 콜백이 동시에 들어와 이미 가입된 경우: 다시 조회해서 쓴다.
            log.info("소셜 회원이 이미 가입되어 있어 기존 계정을 사용합니다({}).", prefix);
        }

        member = loginService.login(username);
        if (member == null) {
            throw new IllegalStateException("소셜 회원 가입 후 조회에 실패했습니다(" + prefix + ").");
        }
        return member;
    }

    /** 비밀번호 검증 대신 소셜 인증을 거쳤으므로 AuthenticationManager는 쓰지 않는다. */
    public void signIn(HttpServletRequest request, LoginDTO member) {
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

    private String trimNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            return null;
        }
        return nickname.length() > NICKNAME_MAX ? nickname.substring(0, NICKNAME_MAX) : nickname;
    }
}
