package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;
import java.util.List;

public interface ReservationService {

    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategory2();

    ArrayList<CategoryVO> getMovieCategory3();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

    int getReservation_board(Criteria cri);
    int movie_report_resist(ReservationVO vo);

    List<ReservationVO> getReservation_pay(Criteria cri);

    void reservation_Delete(ReservationVO vo);

    int paymentVO(PaymentVO vo);

    ArrayList<PaymentVO> getPayment_report();


}
