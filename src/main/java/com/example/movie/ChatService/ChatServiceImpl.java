package com.example.movie.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatService")

public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatMapper chatMapper;
}
