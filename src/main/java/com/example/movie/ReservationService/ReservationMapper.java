package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface ReservationMapper {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategory2();
    ArrayList<CategoryVO> getMovieCategory3();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);
//    보라
    int getReservation_board(Criteria cri);

    ArrayList<ReservationVO> getReservation_pay(Criteria cri);


    int movie_report_resist(ReservationVO vo);

    void reservation_Delete(int reservation_number);


}
