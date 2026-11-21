package com.example.movie.ReservationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 결제하지 않은 오래된 예약을 정리한다. 지우기 전에 포트원을 확인해, 이미 결제된 예약은 확정한다. */
@Slf4j
@Component
public class ReservationExpiryJob {

    private final ReservationService reservationService;
    private final int pendingMinutes;
    private final int batchSize;

    public ReservationExpiryJob(@Qualifier("reservationService") ReservationService reservationService,
                                @Value("${reservation.pending-minutes:10}") int pendingMinutes,
                                @Value("${reservation.expiry-batch-size:100}") int batchSize) {
        this.reservationService = reservationService;
        this.pendingMinutes = pendingMinutes;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${reservation.expiry-check-ms:60000}")
    public void expirePendingReservations() {
        ReservationService.ExpiryResult r = reservationService.expirePendingReservations(pendingMinutes, batchSize);
        if (r.confirmed() + r.released() + r.refunded() + r.skipped() > 0) {
            log.info("예약 만료 처리({}분 경과): 결제 확인 후 확정 {}건, 좌석 해제 {}건, 금액 불일치 환불 {}건, 보류 {}건",
                    pendingMinutes, r.confirmed(), r.released(), r.refunded(), r.skipped());
        }
    }
}
