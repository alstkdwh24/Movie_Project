package com.example.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie/community")
public class EventController {
    @GetMapping("/event")
    public String Event(){
        return "movie/community/event";
    }
}
