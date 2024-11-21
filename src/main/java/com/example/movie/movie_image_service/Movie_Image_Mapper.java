package com.example.movie.movie_image_service;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.commandVO.MainsVO.MovieVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper

public interface Movie_Image_Mapper {
    int event_resist(EventVO_Board vo);

    int movie_resist(MovieVO vo);
    ArrayList<MovieVO> movie_resist_list(MovieVO vo);

}
