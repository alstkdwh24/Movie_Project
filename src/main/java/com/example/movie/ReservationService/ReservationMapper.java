package com.example.movie.ReservationService;

import com.example.movie.entity.*;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ReservationMapper {

    ArrayList<CategoryEntity> getMovieCategory();

    ArrayList<CategoryEntity> getMovieCategory2();

    ArrayList<CategoryEntity> getMovieCategory3();

    ArrayList<CategoryEntity> getMovieCategoryChild(CategoryEntity entity);

    int getReservation_board(Criteria cri);

    List<ReservationEntity> getReservation_pay(Criteria cri);

    int movie_report_resist(ReservationEntity entity);

    void reservation_Delete(ReservationEntity entity);

    int paymentVO(PaymentEntity entity);

    int existsPaymentById(@Param("paymentId") String paymentId);

    ArrayList<PaymentEntity> getPayment_report(String username);

    ReservationEntity paymentId_same(@Param("username") String username, @Param("paymentId") String paymentId);

    // 포트원을 데이터베이스에 저장
    int paymentIdSave(PaymentIdEntity entity);

    ArrayList<PaymentListEntity> PaymentIdList(@Param("paymentId") String paymentId, @Param("username") String username);

    int movie_payment_paymentId(PaymentEntity entity);
}
