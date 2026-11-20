package com.example.movie.ReservationService;

import com.example.movie.commandVO.*;
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

    int reservation_Delete(ReservationVO vo);

    PaymentCompleteResult completePayment(String username, String paymentId);

    int expirePendingReservations(int minutes);

    int paymentVO(PaymentVO vo);

    ArrayList<PaymentVO> getPayment_report(String username);

    ReservationVO paymentId_same(String username,String paymentId);


    int paymentIdSave(PaymentIdVO vo);



    int movie_payment_paymentId(PaymentVO vo);


}
