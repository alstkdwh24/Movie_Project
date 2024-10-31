package com.example.movie.controller;

import com.example.movie.MovieService.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/movie")
public class MovieController  {



    @Autowired
    @Qualifier("movieService")
    public MovieService movieService;

    @GetMapping("/mains")
    public String mains(HttpSession session, Model model, HttpSession session2) {

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        // 세션에서 사용자 정보를 가져옵니다.
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        return "movie/mains";
    }
}
