package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie/community")
public class EventController {
    @Autowired
    @Qualifier()

    @GetMapping("/event")
    public String Event(){
        return "movie/community/event";
    }
    @GetMapping("/gboard")
    public String gboard(){
        return "movie/community/gboard";
    }
    @GetMapping("/freeboard")
    public String FREEBOARD(){
        return "movie/community/freeboard";
    }
}
