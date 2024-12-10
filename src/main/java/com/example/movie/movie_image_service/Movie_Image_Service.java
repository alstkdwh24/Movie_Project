package com.example.movie.movie_image_service;

import com.example.movie.commandVO.MainsVO.EventVO_Board;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.commandVO.MainsVO.Movie_gifticonVO;

import java.util.ArrayList;

public interface Movie_Image_Service {
    int event_resist(EventVO_Board vo);

    int movie_resist(MovieVO vo);

    ArrayList<MovieVO> movie_resist_list(MovieVO vo);

    int event_board_resist(EventVO_Board vo);

    ArrayList<EventVO_Board> get_event_list(EventVO_Board vo);

    int gifticon_resist(Movie_gifticonVO vo);

    ArrayList<Movie_gifticonVO> gifticon_select(Movie_gifticonVO vo);
}
