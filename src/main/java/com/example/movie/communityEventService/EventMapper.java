package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import com.example.movie.util.Criteria;
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

    EventVO freeselect(int free_number);

    EventVO free_detail_update_select(int free_number);

    EventVO count(@Param("free_number") int free_number);


    void free_delete(int free_number);

    EventVO gSelect(int g_number);

    void g_delete(int g_number);

    EventVO g_update(int g_number);
}
