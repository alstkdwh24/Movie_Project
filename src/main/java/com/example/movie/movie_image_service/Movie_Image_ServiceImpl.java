package com.example.movie.movie_image_service;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.commandVO.MainsVO.MovieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("movie_Image_Service")

public class Movie_Image_ServiceImpl implements Movie_Image_Service{

    @Autowired
    private Movie_Image_Mapper movieImageMapper;
    @Override
    public int event_resist(EventVO_Board vo) {
        return movieImageMapper.event_resist(vo);
    }

    @Override
    public int movie_resist(MovieVO vo) {
        return movieImageMapper.movie_resist(vo);
    }

    @Override
    public ArrayList<MovieVO> movie_resist_list(MovieVO vo) {
        // movie_resist_filePath가 null인지 확인
        // 영화 리스트를 조회하여 반환합니다.
        return movieImageMapper.movie_resist_list(vo);
    }

    @Override
    public int event_board_resist(EventVO_Board vo) {
        return movieImageMapper.event_board_resist(vo);
    }

    @Override
    public ArrayList<EventVO_Board> get_event_list(EventVO_Board vo) {
        return movieImageMapper.get_event_list(vo);
    }
}
