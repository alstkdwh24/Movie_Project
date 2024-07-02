package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie/login")
public class LoginController {
    @Autowired
    @Qualifier("loginService")
    public LoginService loginService;
//회원가입 화면
    @GetMapping("/join")
    public String join(){
        return "movie/login/join";
    }
//    로그인 화면
    @GetMapping("/login")
    public String login(){
        return "movie/login/login";
    }




    @PostMapping("/login_join")
    public String login_join(LoginVO vo, RedirectAttributes ra){
        int result =loginService.Login_join(vo);
        if(result ==1){
            ra.addFlashAttribute("msg","정상적으로 처리하였습니다.");

            System.out.println(vo.getId());
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
