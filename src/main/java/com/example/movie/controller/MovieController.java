package com.example.movie.controller;

import com.example.movie.MovieService.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie")
public class MovieController  {

    @Autowired
    @Qualifier("movieService")
    public MovieService movieService;

    @GetMapping("/mains")
    public String mains(){
        return "movie/mains";
    }


    @GetMapping("/main")
    public String main(){
        return "movie/main";
    }
}
