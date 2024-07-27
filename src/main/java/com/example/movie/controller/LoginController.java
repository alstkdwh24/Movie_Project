package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import com.example.movie.SecuritySet.MyUserDetailService;
import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/movie/login")
public class LoginController {
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


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


    @PostMapping("/Login_form")
    public String login_login(HttpServletRequest request,
                              @RequestParam("username") String username,
                              @RequestParam("pw") String pw,
                              Model model,
                              RedirectAttributes ra) {

        LoginVO vo = loginService.login(username);
        if (vo != null && passwordEncoder.matches(pw, vo.getPw())) {
//            session.setAttribute("username",vo.getUsername());
            ra.addFlashAttribute("msg", "정상적으로 로그인되었습니다");
//            System.out.println("session" + session);
            return "redirect:/movie/mains";
        } else {
            ra.addFlashAttribute("msg", "로그인에 실패했습니다.");
            System.out.println("vo.username :" + username);
            System.out.println("pw: " + pw);
            return "redirect:/movie/login/login";
        }
    }


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
}

