package com.example.movie.controller;


import com.example.movie.Movie_Information.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("movie/information")
public class Movie_Information_Controller {

    @Autowired
    @Qualifier("informationService")
    public InformationService informationService;


    @GetMapping("/movie_information")
    public String movie_information(HttpSession session, Model model,HttpSession session2){

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/information/movie_information";
    }
}
