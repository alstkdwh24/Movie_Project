package com.example.movie.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EventService")
public class EventServiceImpl implements EventService{

    @Autowired
    private EventMapper eventMapper;
}
