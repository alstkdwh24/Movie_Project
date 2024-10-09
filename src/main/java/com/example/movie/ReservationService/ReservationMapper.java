package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ReservationMapper {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategory2();
    ArrayList<CategoryVO> getMovieCategory3();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);
//    보라
    int getReservation_board(Criteria cri);

    List<ReservationVO> getReservation_pay(Criteria cri);


    int movie_report_resist(ReservationVO vo);

    void reservation_Delete(ReservationVO vo);

    int paymentVO(PaymentVO vo);


}
