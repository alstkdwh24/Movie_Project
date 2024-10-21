package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.DataInput;
import java.util.ArrayList;

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


    @PostMapping("/payment/complete")
    @CrossOrigin(origins = "*") // 모든 출처 허용
    public ResponseEntity<PaymentVO> paymentVO(@RequestBody PaymentVO vo) {
        int savePayment = reservationService.paymentVO(vo);
        if (savePayment == 1) {
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(vo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movie/payment/report")
    public ResponseEntity<ArrayList<PaymentVO>> payment_report(@RequestParam("username") String username) {
        ArrayList<PaymentVO> paymentReport = reservationService.getPayment_report(username);
        return new ResponseEntity<>(paymentReport, HttpStatus.OK);

    }

    @PostMapping("/api/Tokens")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getToken(@RequestBody TokenVO tokenVO,HttpSession session) {
        String url = "https://api.portone.io/login/api-secret";
        ResponseEntity<?> response;


        try {
            // TokenVO 객체를 사용하여 요청을 보냅니다.
            response = restTemplate.postForEntity(url, tokenVO, Object.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing your request: " + e.getMessage());
        }

        return ResponseEntity.ok(response.getBody()); // 클라이언트에 응답 반환
    }

    @PostMapping("/api/access_token")
    public ResponseEntity<TokenVO> getToken(HttpSession session,
                                           @RequestBody String accessToken
            ) {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken(String.valueOf(accessToken));
        session.setAttribute("token",tokenVO.getAccessToken());
        session.setMaxInactiveInterval(5000);
        System.out.println("토큰"+session.getAttribute("token"));
        System.out.println(session);
        return ResponseEntity.ok(tokenVO); // 성공적으로 토큰을 반환

    }


    @GetMapping("/payments")
    @CrossOrigin(origins = "*")
    public ResponseEntity<PaymentVO> getPayments(HttpSession session) throws JsonProcessingException {
        String url = "https://api.portone.io/payments";

        // 세션에서 TokenVO 객체 가져오기
        String tokenJson = (String) session.getAttribute("token"); // String으로 가져오기

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

        // API 호출
        ResponseEntity<PaymentVO> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, PaymentVO.class);
        } catch (HttpClientErrorException e) {
            System.out.println("HTTP 상태 코드: " + e.getStatusCode());
            System.out.println("응답 본문: " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 401 Unauthorized
        }

        // 응답 반환
        return ResponseEntity.ok(response.getBody());
    }


}


