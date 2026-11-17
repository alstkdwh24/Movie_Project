package com.example.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationEntity {
    private String username;
    private String movieTitle;
    private String movieTime;
    private String moviePlace;
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
