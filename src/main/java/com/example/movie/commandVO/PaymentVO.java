package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentVO {
    private String paymentId;
    private String movie_title;
    private String movie_place;
    private String movie_time;
    private String movie_Seat;
    private String reservation_price;
    private String payment_time;
    private String username;
    private String UserPhone;
//    private String Token;
//    private String imp_uid;
    private String accessToken;
    private List<PaymentResponseVO> payments;
}
