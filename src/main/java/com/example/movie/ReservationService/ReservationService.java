package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface ReservationService {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

    public int resist(ReservationVO vo);


}
