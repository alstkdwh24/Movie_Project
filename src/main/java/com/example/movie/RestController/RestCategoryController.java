package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class RestCategoryController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    // 좌석 가격은 서버가 정한다(화면의 hidden 값은 바꿀 수 있으므로 믿지 않는다)
    @Value("${reservation.seat-price:1000}")
    private int seatPrice;


    @GetMapping("/getMovieCategory")
    public ResponseEntity<ArrayList<CategoryVO>> getMovieCategory(){
        return new ResponseEntity<>(reservationService.getMovieCategory(), HttpStatus.OK);
    }
    @GetMapping("/getMovieCategory2")
    public ResponseEntity<ArrayList<CategoryVO>> getMovieCategory2(){
        return new ResponseEntity<>(reservationService.getMovieCategory2(), HttpStatus.OK);
    }
    @GetMapping("/getMovieCategory3")
    public ResponseEntity<ArrayList<CategoryVO>> getMovieCategory3(){
        return new ResponseEntity<>(reservationService.getMovieCategory3(), HttpStatus.OK);
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
    @PostMapping("/resist_reservation")
    public ResponseEntity<ReservationVO> createReservation(@RequestBody  ReservationVO vo, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        vo.setUsername(principal.getName());
        vo.setReservation_price(String.valueOf(seatPrice));
        // paymentId도 서버가 만든다. 클라이언트 값을 받으면 이미 결제된 paymentId를 다른 좌석에 다시 쓸 수 있다
        vo.setPaymentId(ReservationService.PAYMENT_ID_PREFIX + UUID.randomUUID());
        try {
            int saveReservation = reservationService.movie_report_resist(vo);
            if (saveReservation == 1) {
                return new ResponseEntity<>(vo, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DuplicateKeyException e) {
            // 좌석 유니크 제약(uq_reservation_seat) 위반 = 이미 예매된 좌석
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
