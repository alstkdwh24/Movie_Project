package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationVO {
    private String username;
    private String movieTitle;  // CamelCase로 변경
    private String movieTime;    // CamelCase로 변경
    private String moviePlace;   // CamelCase로 변경
    private Integer reservation_number;
    private String movieSeat;
    private String movie_title;
    private String movie_place;
    private String movie_time;
    private String movie_Seat;
    private String reservation_price;
    private String UserPhone;
    private String paymentId;
}