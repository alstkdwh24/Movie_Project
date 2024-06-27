package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface EventService {
    public int gallery_free_board(EventVO vo);
    public int gallery_free_total(Criteria cri);
    public ArrayList<EventVO> gallery_free_show(Criteria cri);


    }
