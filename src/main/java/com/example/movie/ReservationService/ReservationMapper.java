package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ReservationMapper {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);
}
