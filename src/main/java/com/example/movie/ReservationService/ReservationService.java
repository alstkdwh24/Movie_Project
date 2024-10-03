package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface ReservationService {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategory2();

    ArrayList<CategoryVO> getMovieCategory3();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

    int getReservation_board(Criteria cri);
    int movie_report_resist(ReservationVO vo);

    ArrayList<ReservationVO> getReservation_pay(Criteria cri);

    void reservation_Delete(int reservation_number);


}
