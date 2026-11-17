package com.example.movie.ReservationService;

import com.example.movie.SecuritySet.MyUserDetail;
import com.example.movie.dto.*;
import com.example.movie.util.Criteria;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public interface ReservationService {

    ArrayList<CategoryDTO> getMovieCategory();

    ArrayList<CategoryDTO> getMovieCategory2();

    ArrayList<CategoryDTO> getMovieCategory3();

    ArrayList<CategoryDTO> getMovieCategoryChild(CategoryDTO dto);

    int getReservation_board(Criteria cri);

    int movie_report_resist(ReservationDTO dto);

    List<ReservationDTO> getReservation_pay(Criteria cri);

    void reservation_Delete(ReservationDTO dto);

    ResponseEntity<?> payment_loading(PaymentDTO dto, MyUserDetail userDetail, HttpSession session);

    PaymentDTO completePayment(PaymentDTO dto, String username, String tokenJson);

    ArrayList<PaymentDTO> getPayment_report(String username);

    ReservationDTO paymentId_same(String username, String paymentId);

    int paymentIdSave(PaymentIdDTO dto);

    ArrayList<PaymentListDTO> PaymentIdList(String paymentId, String username);

    int movie_payment_paymentId(PaymentDTO dto);
}
