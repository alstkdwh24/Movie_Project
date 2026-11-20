package com.example.movie.RestController;

import com.example.movie.ReservationService.PaymentCompleteResult;
import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin("*")
public class RestPaymentController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    private final RestTemplate restTemplate;

    public RestPaymentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * 결제 확정. 클라이언트는 paymentId만 보낸다.
     * 서버가 포트원에서 결제를 직접 조회해 상태(PAID)와 금액을 DB 예약과 비교한 뒤에만 저장한다.
     */
    @PostMapping("/payment/complete")
    public ResponseEntity<Void> completePayment(@RequestBody PaymentVO vo, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (vo.getPaymentId() == null || vo.getPaymentId().isBlank()) {
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
            case NOT_PAID, AMOUNT_MISMATCH, NOT_PENDING -> ResponseEntity.status(HttpStatus.CONFLICT).build();
        };
    }

    @GetMapping("/movie/payment/report")
    public ResponseEntity<ArrayList<PaymentVO>> payment_report(@RequestParam("username") String username) {
        ArrayList<PaymentVO> paymentReport = reservationService.getPayment_report(username);
        return new ResponseEntity<>(paymentReport, HttpStatus.OK);

    }


    @PostMapping("/api/access_token")
    public ResponseEntity<TokenVO> getToken(HttpSession session2,
                                            @RequestBody String accessToken
    ) {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(String.valueOf(accessToken));
        session2.setAttribute("token", tokenVO.getAccessToken());
        session2.setMaxInactiveInterval(5000);
        System.out.println("토큰" + session2.getAttribute("token"));
        System.out.println(session2);
        return ResponseEntity.ok(tokenVO); // 성공적으로 토큰을 반환

    }


    @GetMapping("/payments")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<PaymentVO>> getPayments(HttpSession session2) throws JsonProcessingException {
        String url = "https://api.portone.io/payments";

        // 세션에서 TokenVO 객체 가져오기
        String tokenJson = (String) session2.getAttribute("token"); // String으로 가져오기

        // null 체크
        if (tokenJson == null) {
            System.out.println("세션에 'token'이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 401 Unauthorized
        }

        // JSON 문자열을 TokenVO 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        TokenVO tokenVO = objectMapper.readValue(tokenJson, TokenVO.class);

        // 액세스 토큰 가져오기
        String accessToken = tokenVO.getAccessToken();
        if (accessToken == null) {
            System.out.println("accessToken이 null입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 401 Unauthorized
        }

        System.out.println("tokensVO: " + tokenVO);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // 요청 엔티티 생성
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<PaymentResponseVO> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<PaymentResponseVO>() {
                    });
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP 상태 코드: " + e.getStatusCode());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }

        // 응답 반환
        List<PaymentVO> payments = Objects.requireNonNull(response.getBody()).getPayments();
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>()); // 빈 리스트 반환
        }
        // 응답 반환
        return ResponseEntity.ok(payments);
    }


    //paymentIdVO List
    @GetMapping("/paymentIdVOList")
    public ResponseEntity<ArrayList<PaymentId_ListVO>> paymentId_ListVO(@RequestParam("paymentId") String paymentId){
        ArrayList<PaymentId_ListVO> PaymentIdList=reservationService.PaymentIdList(paymentId);
        return new ResponseEntity<>(PaymentIdList,HttpStatus.OK);

    }
}
