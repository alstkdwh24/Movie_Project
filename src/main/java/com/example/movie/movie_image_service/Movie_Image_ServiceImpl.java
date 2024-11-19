package com.example.movie.movie_image_service;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("movie_Image_Service")

public class Movie_Image_ServiceImpl implements Movie_Image_Service{

    @Autowired
    private Movie_Image_Mapper movieImageMapper;
    @Override
    public int event_resist(EventVO_Board vo) {
        return movieImageMapper.event_resist(vo);
    }
}
