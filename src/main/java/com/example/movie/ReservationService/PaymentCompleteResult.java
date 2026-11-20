package com.example.movie.ReservationService;

/** 결제 확정 결과. 컨트롤러가 HTTP 상태로 바꾼다. */
public enum PaymentCompleteResult {
    COMPLETED,             // 포트원 결제 확인, 금액 일치, 예약 PAID로 변경
    RESERVATION_NOT_FOUND, // 본인의 결제 전 예약이 없음(남의 결제, 만료로 삭제, 이미 결제)
    NOT_PAID,              // 포트원에 결제가 없거나 PAID가 아님
    AMOUNT_MISMATCH,       // 포트원 결제 금액 ≠ 예약 금액
    NOT_PENDING            // 확인하는 사이 다른 요청이 확정했거나 만료로 지워짐
}
