package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.commandVO.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<?> getToken(@RequestBody TokenVO tokenVO) {
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
}

//    @PostMapping("/getToken")
//    @CrossOrigin(origins = "*")
//    public ResponseEntity<?> getToken(@RequestBody TokenVO tokenRequest) {
//        String url = "https://api.iamport.kr/users/getToken";
//
//        // IAMPORT API에 요청 보낼 데이터
//        TokenVO request = new TokenVO(tokenRequest.getImp_key(), tokenRequest.getImp_secret());
//
//        // API 요청
//        ResponseEntity<?> response = restTemplate.postForEntity(url, request, Object.class);
//        System.out.println("ascde"+response);
//
//        return ResponseEntity.ok(response.getBody()); // 클라이언트에 응답 반환
//    }
