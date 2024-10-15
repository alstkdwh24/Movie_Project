package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.communityEventService.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RestPaymentController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

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



}
