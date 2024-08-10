package com.example.movie.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/movie/user")
public class UserController {

    @GetMapping("Mypage")
    public String Mypage(HttpSession session, Model model){
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        return "movie/user/Mypage";
    }

    @GetMapping("/myproduct")
    public String myproduct(HttpSession session, Model model){
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/user/myproduct";
    }

}
