package com.example.movie.controller;


import com.example.movie.Movie_Information.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("movie/information")
public class Movie_Information_Controller {

    @Autowired
    @Qualifier("informationService")
    public InformationService informationService;


    @GetMapping("/movie_information")
    public String movie_information(){
        return "/movie/information/movie_information";
    }
}
