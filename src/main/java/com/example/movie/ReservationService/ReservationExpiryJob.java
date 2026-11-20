package com.example.movie.ReservationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 좌석만 잡고 결제하지 않은 예약을 지워 좌석을 다시 풀어 준다. */
@Slf4j
@Component
public class ReservationExpiryJob {

    private final ReservationService reservationService;
    private final int pendingMinutes;

    public ReservationExpiryJob(@Qualifier("reservationService") ReservationService reservationService,
                                @Value("${reservation.pending-minutes:10}") int pendingMinutes) {
        this.reservationService = reservationService;
        this.pendingMinutes = pendingMinutes;
    }

    @Scheduled(fixedDelayString = "${reservation.expiry-check-ms:60000}")
    public void expirePendingReservations() {
        int deleted = reservationService.expirePendingReservations(pendingMinutes);
        if (deleted > 0) {
            log.info("결제하지 않은 예약 {}건 만료({}분 경과)", deleted, pendingMinutes);
        }
    }
}
