package com.example.movie.controller;

import com.example.movie.communityEventService.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie/community")
public class EventController {
    @Autowired
    @Qualifier("EventService")
    public EventService eventService;

    @GetMapping("/event")
    public String Event2(){
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

    @GetMapping("/g_board_writer")
    public  String  g_board(){
        return "movie/community/g_board_writer";
    }

    @GetMapping("/free_board_writer")
    public  String free_board_writer(){
        return "movie/community/free_board_writer";
    }



}
