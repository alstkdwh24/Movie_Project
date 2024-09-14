package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import com.example.movie.SecuritySet.MyUserDetailService;
import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/movie/login")
public class LoginController {
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService; // UserDetailsService 주입

    @Autowired
    private AuthenticationManager authenticationManager;
    //회원가입 화면
    @GetMapping("/join")
    public String join() {
        return "movie/login/join";
    }

    //    로그인 화면
    @GetMapping("/login")
    public String login() {
        return "movie/login/login";
    }

    @GetMapping("/LoginTwo")
    public String Logins(){
        return "movie/login/LoginTwo";
    }



    @GetMapping("/name")
    public @ResponseBody String an(HttpSession session){
        String username=(String) session.getAttribute("username");
        if(username!=null){
            return "세셔은 살아있다.";
        }else{
            return "세션살려";
        }
    }

    @PostMapping("/Login_form")
    public String login_login(HttpServletRequest request,
                              HttpSession session,
                              @RequestParam("username") String username,
                              @RequestParam("pw") String pw,
                              Model model,
                              RedirectAttributes ra) {

        LoginVO vo = loginService.login(username);

        if (vo != null && passwordEncoder.matches(pw, vo.getPw())) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//            // 사용자 인증 정보 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, pw, userDetails.getAuthorities());
//
//            // 인증을 수행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//
            // 인증 성공 후, SecurityContext에 Authentication 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 사용자 정보를 저장 (선택 사항)
            session.setAttribute("user", userDetails);

            session.setMaxInactiveInterval(2000);

            ra.addFlashAttribute("msg", "정상적으로 로그인되었습니다");
            return "redirect:/movie/mains";
        } else {
            ra.addFlashAttribute("msg", "로그인에 실패했습니다.");
            System.out.println("vo.username :" + username);
            System.out.println("pw: " + pw);
            return "redirect:/movie/login/login";
        }
    }


//회원가입 요청 
    @PostMapping("/login_join")
    public String login_join(LoginVO vo, RedirectAttributes ra){
        int result =loginService.Login_join(vo);
        if(result ==1){
            ra.addFlashAttribute("msg","정상적으로 처리하였습니다.");

            System.out.println(vo.getUsername());
        }else{
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }

        return "redirect:/movie/mains";
    }
    @PostMapping("/return_movie_mains")
    public String return_movie_mains(){
        return "redirect:/movie/mains";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/mains"; // 로그아웃 후 홈으로 리다이렉트
    }


}


