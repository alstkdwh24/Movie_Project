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
}
