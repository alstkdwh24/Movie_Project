package com.example.movie.RestController;

import com.example.movie.ReservationService.PaymentCompleteResult;
import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.*;
import com.example.movie.util.PortOneWebhookVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;

@Slf4j
@RestController
public class RestPaymentController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    @Autowired
    private PortOneWebhookVerifier webhookVerifier;

    private static final int MAX_WEBHOOK_BODY_BYTES = 64 * 1024; // 포트원 웹훅 본문은 수백 바이트

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    /**
     * 포트원 결제 승인 웹훅. 브라우저가 확정 요청을 못 보낸 경우(창 닫음 등)에도 확정하거나 환불한다.
     * 서명이 맞는 요청만 받고, 본문을 믿지 않고 포트원에서 결제를 다시 조회한다(completePayment와 같은 검증).
     * 처리하지 못하면 5xx로 답해 포트원이 다시 보내게 한다.
     */
    @PostMapping("/payment/webhook")
    public ResponseEntity<Void> paymentWebhook(HttpServletRequest request) throws IOException {
        // 인증 없는 공개 주소라, 서명 검증 전에 본문을 통째로 메모리에 올리지 않도록 크기를 제한한다
        byte[] raw = request.getInputStream().readNBytes(MAX_WEBHOOK_BODY_BYTES + 1);
        if (raw.length > MAX_WEBHOOK_BODY_BYTES) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        return paymentWebhook(new String(raw, StandardCharsets.UTF_8), request.getHeader("webhook-id"),
                request.getHeader("webhook-timestamp"), request.getHeader("webhook-signature"));
    }

    public ResponseEntity<Void> paymentWebhook(String body, String webhookId, String webhookTimestamp, String webhookSignature) {
        if (!webhookVerifier.verify(webhookId, webhookTimestamp, webhookSignature, body)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        JsonNode message;
        try {
            message = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
        String paymentId = message.path("data").path("paymentId").asText("");
        // 모르는 type, 다른 결제 이벤트, 이 서버가 만들지 않은 결제는 무시한다(문서: 모르는 type은 오류 없이 무시)
        if (!"Transaction.Paid".equals(message.path("type").asText()) || !ReservationService.isServerPaymentId(paymentId)) {
            return ResponseEntity.ok().build();
        }

        PaymentCompleteResult result;
        try {
            result = reservationService.handlePaidWebhook(paymentId);
        } catch (RestClientException | IllegalStateException e) {
            log.error("웹훅 처리 중 포트원 조회 실패 paymentId={}", paymentId, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        log.info("결제 웹훅 처리 paymentId={} 결과={}", paymentId, result);
        // 환불 실패만 재전송을 받는다. 다시 와도 이미 취소된 결제는 PAID가 아니라 중복 취소되지 않는다
        return result == PaymentCompleteResult.REFUND_FAILED
                ? ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                : ResponseEntity.ok().build();
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
