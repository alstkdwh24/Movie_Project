package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.dto.CategoryDTO;
import com.example.movie.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestCategoryController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    @GetMapping("/getMovieCategory")
    public ResponseEntity<ArrayList<CategoryDTO>> getMovieCategory() {
        return new ResponseEntity<>(reservationService.getMovieCategory(), HttpStatus.OK);
    }

    @GetMapping("/getMovieCategory2")
    public ResponseEntity<ArrayList<CategoryDTO>> getMovieCategory2() {
        return new ResponseEntity<>(reservationService.getMovieCategory2(), HttpStatus.OK);
    }

    @GetMapping("/getMovieCategory3")
    public ResponseEntity<ArrayList<CategoryDTO>> getMovieCategory3() {
        return new ResponseEntity<>(reservationService.getMovieCategory3(), HttpStatus.OK);
    }

    @GetMapping("/getMovieCategoryChild/{group_id}/{movie_category_lv}/{movie_detail_lv}")
    public ResponseEntity<ArrayList<CategoryDTO>> getMovieCategoryChild(@PathVariable("group_id") String group_id,
                                                                        @PathVariable("movie_category_lv") Integer movie_category_lv,
                                                                        @PathVariable("movie_detail_lv") Integer movie_detail_lv) {
        CategoryDTO vo = CategoryDTO.builder()
                .group_id(group_id)
                .movie_category_lv(movie_category_lv)
                .movie_detail_lv(movie_detail_lv)
                .build();
        return new ResponseEntity<>(reservationService.getMovieCategoryChild(vo), HttpStatus.OK);
    }

    @PostMapping("/resist_reservation")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO vo) {
        try {
            int saveReservation = reservationService.movie_report_resist(vo);

            if (saveReservation == 1) {
                return new ResponseEntity<>(vo, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (DuplicateKeyException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 이미 예매된 좌석
        }
    }
}
