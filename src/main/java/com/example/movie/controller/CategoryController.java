package com.example.movie.controller;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CategoryController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;


    @GetMapping("/getMovieCategory")
    public ResponseEntity<ArrayList<CategoryVO>> getMovieCategory(){
        return new ResponseEntity<>(reservationService.getMovieCategory(), HttpStatus.OK);
    }

    @GetMapping("/getMovieCategoryChild/{group_id}/{movie_category_lv}/{movie_detail_lv}")
    public ResponseEntity<ArrayList<CategoryVO>> getMovieCategoryChild(@PathVariable("group_id") String group_id,
                                                                        @PathVariable("movie_category_lv") Integer movie_category_lv,
                                                                       @PathVariable("movie_detail_lv") Integer movie_detail_lv){
        CategoryVO vo=CategoryVO.builder().group_id(group_id)
                .movie_category_lv(movie_category_lv)
                .movie_detail_lv(movie_detail_lv)
                .build();
        return new ResponseEntity<>(reservationService.getMovieCategoryChild(vo), HttpStatus.OK);
    }


}
