package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface EventService {
    int gallery_free_board(EventVO vo);
    int gallery_free_total(Criteria cri);
    ArrayList<EventVO> gallery_free_show(Criteria cri);



    int gallery_g_board(EventVO vo);
    int gallery_g_total(Criteria cri);
    ArrayList<EventVO> gallery_g_show(Criteria cri);


    }
