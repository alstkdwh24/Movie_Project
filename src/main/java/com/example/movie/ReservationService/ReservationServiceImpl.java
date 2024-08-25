package com.example.movie.ReservationService;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    public int movie_report_resist(ReservationVO vo) {
        return reservationMapper.movie_report_resist(vo);
    }

}
