package com.example.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {
    private String paymentId;
    private String movie_title;
    private String movie_place;
    private String movie_time;
    private String movie_Seat;
    private String reservation_price;
    private String payment_time;
    private String username;
    private String UserPhone;
    private String accessToken;
    private String payment_time_two;
}
