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

    EventVO freeselect(int free_number);

    int  findEventFree_Number(int freeNumber);

    EventVO free_detail_update_select(int free_number);

    void free_delete(int free_number);

    EventVO gSelect(int g_number);

    void g_delete(int g_number);

    EventVO g_update(int g_number);

ArrayList<EventVO> get_free_comment( );
}
