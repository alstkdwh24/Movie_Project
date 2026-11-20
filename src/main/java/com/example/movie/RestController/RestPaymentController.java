package com.example.movie.RestController;

import com.example.movie.ReservationService.PaymentCompleteResult;
import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.security.Principal;
import java.util.ArrayList;

@Slf4j
@RestController
public class RestPaymentController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;



    /**
     * 결제 확정. 클라이언트는 paymentId만 보낸다.
     * 서버가 포트원에서 결제를 직접 조회해 상태(PAID)와 금액을 DB 예약과 비교한 뒤에만 저장한다.
     */
    @PostMapping("/payment/complete")
    public ResponseEntity<Void> completePayment(@RequestBody PaymentVO vo, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 포트원 경로에 들어가므로 문자 종류를 제한한다(/ . 등으로 다른 결제 경로를 만들지 못하게)
        if (vo.getPaymentId() == null || !vo.getPaymentId().matches("[A-Za-z0-9_-]{1,64}")) {
            return ResponseEntity.badRequest().build();
        }
        PaymentCompleteResult result;
        try {
            result = reservationService.completePayment(principal.getName(), vo.getPaymentId());
        } catch (RestClientException | IllegalStateException e) {
            log.error("포트원 결제 조회 실패 paymentId={}", vo.getPaymentId(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return switch (result) {
            case COMPLETED -> ResponseEntity.status(HttpStatus.CREATED).build();
            case RESERVATION_NOT_FOUND -> ResponseEntity.notFound().build();
            case NOT_PAID, NOT_PENDING -> ResponseEntity.status(HttpStatus.CONFLICT).build();
            case REFUNDED -> ResponseEntity.status(HttpStatus.GONE).build();
            case REFUND_FAILED -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        };
    }

    /** 결제 내역: 로그인한 본인 것만. 예전에는 username 파라미터로 아무나 남의 내역을 볼 수 있었다 */
    @GetMapping("/movie/payment/report")
    public ResponseEntity<ArrayList<PaymentVO>> payment_report(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(reservationService.getPayment_report(principal.getName()));
    }

    // 제거한 API(호출하는 화면 없음):
    // /api/access_token, /payments  - 아무나 세션에 토큰을 넣어 포트원 결제 목록을 프록시하고 토큰을 로그에 남김.
    //                                  결제 확인은 서버 PortOneClient 단건 조회로 대체
    // /paymentIdVOList              - WHERE 없이 전체 사용자의 결제 내역을 로그인 없이 반환
}
