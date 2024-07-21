package com.example.movie2.communityEventService;

import com.example.movie2.commandVO.EventVO;
import com.example.movie2.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface EventMapper {
    int gallery_free_board(EventVO vo);
    ArrayList<EventVO> gallery_free_show(Criteria cri);

    int gallery_free_total(Criteria cri);

    int gallery_g_board(EventVO vo);

    ArrayList<EventVO> gallery_g_show(Criteria cri);

    int gallery_g_total(Criteria cri);

    public EventVO freeselect(int free_number);

    public EventVO free_detail_update_select(int free_number);

    public void free_delete(int free_number);

    public EventVO gSelect(int g_number);

    public void g_delete(int g_number);

    public EventVO g_update(int g_number);
}
