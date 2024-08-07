package com.example.movie2.communityEventService;

import com.example.movie2.commandVO.EventVO;
import com.example.movie2.util.Criteria;

import java.util.ArrayList;

public interface EventService {
    int gallery_free_board(EventVO vo);
    int gallery_free_total(Criteria cri);
    ArrayList<EventVO> gallery_free_show(Criteria cri);



    int gallery_g_board(EventVO vo);
    int gallery_g_total(Criteria cri);
    ArrayList<EventVO> gallery_g_show(Criteria cri);

    EventVO freeselect(int free_number);

    public EventVO free_detail_update_select(int free_number);

    public void free_delete(int free_number);

    public EventVO gSelect(int g_number);

    public void g_delete(int g_number);

    public EventVO g_update(int g_number);


}
