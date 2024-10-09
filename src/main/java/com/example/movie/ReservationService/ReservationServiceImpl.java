package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("reservationService")
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public ArrayList<CategoryVO> getMovieCategory() {
        return reservationMapper.getMovieCategory();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategory2() {
        return reservationMapper.getMovieCategory2();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategory3() {
        return reservationMapper.getMovieCategory3();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo) {
        return reservationMapper.getMovieCategoryChild(vo);
    }

    @Override
    public int getReservation_board(Criteria cri) {
        return reservationMapper.getReservation_board(cri);
    }

    @Override
    public int movie_report_resist(ReservationVO vo) {
        return reservationMapper.movie_report_resist(vo);
    }

    @Override
    public List<ReservationVO> getReservation_pay(Criteria cri) {


        return reservationMapper.getReservation_pay(cri);
    }

    @Override
    public void reservation_Delete(ReservationVO vo) {
        reservationMapper.reservation_Delete(vo);
    }

    @Override
    public int paymentVO(PaymentVO vo) {
        return reservationMapper.paymentVO(vo);
    }

}
