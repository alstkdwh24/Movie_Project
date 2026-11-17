package com.example.movie.dto;

import com.example.movie.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
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
    private List<PaymentResponseDTO> payments;
    private String payment_time_two;

    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
                .paymentId(this.paymentId)
                .movie_title(this.movie_title)
                .movie_place(this.movie_place)
                .movie_time(this.movie_time)
                .movie_Seat(this.movie_Seat)
                .reservation_price(this.reservation_price)
                .payment_time(this.payment_time)
                .username(this.username)
                .UserPhone(this.UserPhone)
                .accessToken(this.accessToken)
                .payment_time_two(this.payment_time_two)
                .build();
    }

    public static PaymentDTO fromEntity(PaymentEntity entity) {
        if (entity == null) return null;
        return PaymentDTO.builder()
                .paymentId(entity.getPaymentId())
                .movie_title(entity.getMovie_title())
                .movie_place(entity.getMovie_place())
                .movie_time(entity.getMovie_time())
                .movie_Seat(entity.getMovie_Seat())
                .reservation_price(entity.getReservation_price())
                .payment_time(entity.getPayment_time())
                .username(entity.getUsername())
                .UserPhone(entity.getUserPhone())
                .accessToken(entity.getAccessToken())
                .payment_time_two(entity.getPayment_time_two())
                .build();
    }
}
