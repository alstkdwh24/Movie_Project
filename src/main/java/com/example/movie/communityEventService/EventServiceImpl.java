package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("EventService")
public class EventServiceImpl implements EventService{

    @Autowired
    private EventMapper eventMapper;

    public int gallery_free_board(EventVO vo){
        return eventMapper.gallery_free_board(vo);
    }

    @Override
    public int gallery_free_total(Criteria cri) {
        return eventMapper.gallery_free_total(cri);
    }

    @Override
    public ArrayList<EventVO> gallery_free_show(Criteria cri) {
        return eventMapper.gallery_free_show(cri);
    }

    public int gallery_g_board(EventVO vo){
        return eventMapper.gallery_g_board(vo);
    }

    @Override
    public int gallery_g_total(Criteria cri) {
        return eventMapper.gallery_g_total(cri);
    }

    @Override
    public ArrayList<EventVO> gallery_g_show(Criteria cri) {
        return eventMapper.gallery_g_show(cri);
    }
}
