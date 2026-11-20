package com.example.movie.ReservationService;

/** 결제 확정 결과. 컨트롤러가 HTTP 상태로 바꾼다. */
public enum PaymentCompleteResult {
    COMPLETED,             // 포트원 결제 확인, 금액 일치, 예약 PAID로 변경
    RESERVATION_NOT_FOUND, // 본인의 결제 전 예약이 없음(남의 결제, 만료로 삭제, 이미 결제)
    NOT_PAID,              // 포트원에 결제가 없거나 PAID가 아님
    NOT_PENDING,           // 확인하는 사이 다른 요청이 확정했거나, 이미 다른 예약 확정에 쓴 결제
    REFUNDED,              // 예약이 만료로 지워졌거나 금액이 달라 결제를 자동 취소(환불)함
    REFUND_FAILED          // 자동 취소 요청이 실패함. 운영자가 확인해야 한다
}
