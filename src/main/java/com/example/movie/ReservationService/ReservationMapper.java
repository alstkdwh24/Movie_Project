package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ReservationMapper {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

    int movie_report_resist(ReservationVO vo);

}
