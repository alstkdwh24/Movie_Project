package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventMapper {
    public int gallery_free_board(EventVO vo);

}
