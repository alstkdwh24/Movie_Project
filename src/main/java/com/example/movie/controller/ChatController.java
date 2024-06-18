package com.example.movie.controller;

import com.example.movie.ChatService.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("movie/chats")
public class ChatController {
    @Autowired
    @Qualifier("chatService")
    public ChatService chatService;


    @GetMapping("/chat")
    public String Chat(){
        return "movie/chats/chat";
    }

}
