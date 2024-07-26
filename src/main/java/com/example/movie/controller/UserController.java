package com.example.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie/user")
public class UserController {

    @GetMapping("Mypage")
    public String Mypage(){
        return "movie/user/Mypage";
    }

    @GetMapping("/myproduct")
    public String myproduct(){
        return "movie/user/myproduct";
    }

}
