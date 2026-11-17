package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import com.example.movie.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequestMapping("/movie/login")
public class LoginController {
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 회원가입 화면
    @GetMapping("/join")
    public String join(HttpSession session2, Model model) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);
        return "movie/login/join";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String login(HttpSession session2, Model model) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);
        return "movie/login/login";
    }

    @GetMapping("/LoginTwo")
    public String Logins() {
        return "movie/login/LoginTwo";
    }

    @GetMapping("/name")
    public @ResponseBody String an(HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username != null) {
            return "세셔은 살아있다.";
        } else {
            return "세션살려";
        }
    }

    @PostMapping("/Login_form")
    public String login_login(HttpServletRequest request,
                              HttpSession session,
                              HttpSession session2,
                              @RequestParam("username") String username,
                              @RequestParam("pw") String pw,
                              Model model,
                              RedirectAttributes ra) {

        LoginDTO vo = loginService.login(username);

        if (vo != null && passwordEncoder.matches(pw, vo.getPw())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, pw, userDetails.getAuthorities());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            session.setAttribute("user", userDetails);
            session.setMaxInactiveInterval(2000);

            session2.setAttribute("roles", vo.getRoles());
            session2.setMaxInactiveInterval(2000);

            System.out.println("roless" + vo.getRoles());

            ra.addFlashAttribute("msg", "정상적으로 로그인되었습니다");
            return "redirect:/movie/mains";
        } else {
            ra.addFlashAttribute("msg", "로그인에 실패했습니다.");
            System.out.println("vo.username :" + username);
            return "redirect:/movie/login/login";
        }
    }

    // 회원가입 요청
    @PostMapping("/login_join")
    public String login_join(LoginDTO vo, RedirectAttributes ra) {
        // kakao_ 아이디는 카카오 로그인 전용이다. 일반 가입으로 먼저 만들어 두면 카카오 사용자가 그 계정으로 로그인하게 된다(계정 선점).
        if (vo.getUsername() == null || vo.getUsername().toLowerCase(Locale.ROOT).startsWith("kakao_")) {
            ra.addFlashAttribute("msg", "사용할 수 없는 아이디입니다.");
            return "redirect:/movie/login/login";
        }
        int result = loginService.Login_join(vo);
        if (result == 1) {
            ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
            System.out.println(vo.getUsername());
        } else {
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }

        return "redirect:/movie/mains";
    }

    @PostMapping("/return_movie_mains")
    public String return_movie_mains() {
        return "redirect:/movie/mains";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpSession session2) {
        session.invalidate();
        session2.invalidate();
        return "redirect:/mains";
    }
}
