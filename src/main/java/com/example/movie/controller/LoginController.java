package com.example.movie.controller;

import com.example.movie.LoginService.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
