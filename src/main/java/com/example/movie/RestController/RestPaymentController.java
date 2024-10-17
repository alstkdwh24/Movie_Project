package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.commandVO.TokenVO;
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

    private final  RestTemplate restTemplate;

    public RestPaymentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostMapping("/payment/complete")
    @CrossOrigin(origins = "*") // 모든 출처 허용
    public ResponseEntity<PaymentVO> paymentVO(@RequestBody PaymentVO vo){
        int savePayment=reservationService.paymentVO(vo);
        if(savePayment==1){
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else{
           return new ResponseEntity<>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/movie/payment/report")
    public ResponseEntity<ArrayList<PaymentVO>> payment_report(@RequestParam("username") String username){
ArrayList<PaymentVO> paymentReport=reservationService.getPayment_report(username);
return new ResponseEntity<>(paymentReport,HttpStatus.OK);

    }
    @GetMapping("/payment/status")
    public ResponseEntity<?> pay_same(@RequestParam("username") String username,@RequestParam("paymentId")String paymentId){
        ReservationVO paymentId_same=reservationService.paymentId_same(username,paymentId);
        return ResponseEntity.ok(paymentId_same);
    }

    @PostMapping("/getToken")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> getToken(@RequestBody TokenVO tokenRequest) {
        String url = "https://api.iamport.kr/users/getToken";

        // IAMPORT API에 요청 보낼 데이터
        TokenVO request = new TokenVO(tokenRequest.getImp_key(), tokenRequest.getImp_secret());

        // API 요청
        ResponseEntity<?> response = restTemplate.postForEntity(url, request, Object.class);
        System.out.println("ascde"+response);

        return ResponseEntity.ok(response.getBody()); // 클라이언트에 응답 반환
    }

    @GetMapping("/payments/{imp_uid}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<PaymentVO> getPayments_report(  @PathVariable("imp_uid") String impUid,
                                                          @RequestParam("token") String token

                                                       ){
        System.out.println(impUid);
        System.out.println("token"+token);
        String url = "https://api.iamport.kr/payments/"+ impUid;
        System.out.println(url+"url");

        HttpHeaders headers=new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);

        HttpEntity<String> entity=new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();


        try {
            ResponseEntity<PaymentVO> response = restTemplate.exchange(url, HttpMethod.GET, entity, PaymentVO.class);

            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("API 호출 실패: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            System.err.println("기타 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }



}
