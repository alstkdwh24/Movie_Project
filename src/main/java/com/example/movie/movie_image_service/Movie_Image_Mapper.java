package com.example.movie.movie_image_service;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface Movie_Image_Mapper {
    int event_resist(EventVO_Board vo);
}
