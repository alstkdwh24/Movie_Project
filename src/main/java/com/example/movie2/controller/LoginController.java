package com.example.movie2.controller;

import com.example.movie2.LoginService.LoginService;
import com.example.movie2.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/LoginTwo")
    public String loginTwo(){
        return "/movie/login/LoginTwo";
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

    @PostMapping("/login_div")
    public String login_div(@RequestParam("id") String id, @RequestParam("pw") String pw, RedirectAttributes ra){
LoginVO vo =loginService.login(id);
        int result = 0;
        if (vo.getPw().equals(pw)) { // 일반 사용자 로그인 성공
            result = 1;}
        if (result == 1) {
            ra.addFlashAttribute("msg", "로그인 되었습니다");

            // 일반 사용자 권한으로 처리할 로직 추가
            return "redirect:/movie/mains";

        }

        else{
            ra.addFlashAttribute("msg", "로그인안되었습니다");
            return "redirect:/movie/login/login";
        }
    }
    @PostMapping("/return_movie_mains")
    public String return_movie_mains(){
        return "redirect:/movie/mains";
    }
}
