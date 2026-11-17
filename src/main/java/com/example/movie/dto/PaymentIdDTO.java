package com.example.movie.dto;

import com.example.movie.entity.PaymentIdEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentIdDTO {
    private String paymentId;
    private LocalDateTime payment_time;
    private String reservation_price;
    private String username;

    public PaymentIdEntity toEntity() {
        return PaymentIdEntity.builder()
                .paymentId(this.paymentId)
                .payment_time(this.payment_time)
                .reservation_price(this.reservation_price)
                .username(this.username)
                .build();
    }

    public static PaymentIdDTO fromEntity(PaymentIdEntity entity) {
        if (entity == null) return null;
        return PaymentIdDTO.builder()
                .paymentId(entity.getPaymentId())
                .payment_time(entity.getPayment_time())
                .reservation_price(entity.getReservation_price())
                .username(entity.getUsername())
                .build();
    }
}
