package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;

import java.util.ArrayList;

public interface ReservationService {

    public ArrayList<CategoryVO> getMovieCategory();

    public ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

}
