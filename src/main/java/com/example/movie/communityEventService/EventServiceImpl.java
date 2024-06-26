package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EventService")
public class EventServiceImpl implements EventService{

    @Autowired
    private EventMapper eventMapper;

    public int gallery_free_board(EventVO vo){
        return eventMapper.gallery_free_board(vo);
    }
}
