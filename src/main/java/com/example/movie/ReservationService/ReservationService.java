package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;

import java.util.ArrayList;

public interface ReservationService {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

}
