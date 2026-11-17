package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.SecuritySet.MyUserDetail;
import com.example.movie.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
public class PaymentApi {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    private final RestTemplate restTemplate;

    public PaymentApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/payment/complete")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> completePayment(@RequestBody PaymentDTO dto,
                                             @AuthenticationPrincipal MyUserDetail userDetail,
                                             HttpSession session) {
        return reservationService.payment_loading(dto, userDetail, session);
    }

    @GetMapping("/movie/payment/report")
    public ResponseEntity<ArrayList<PaymentDTO>> getPaymentReport(@RequestParam("username") String username) {
        ArrayList<PaymentDTO> list = reservationService.getPayment_report(username);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/api/Tokens")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> requestToken(@RequestBody TokenDTO tokenDto, HttpSession session) {
        String url = "https://api.portone.io/login/api-secret";
        session.setAttribute("accessToken", tokenDto.getAccessToken());

        try {
            ResponseEntity<?> response = restTemplate.postForEntity(url, tokenDto, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while processing your request: " + e.getMessage());
        }
    }

    @PostMapping("/api/access_token")
    public ResponseEntity<TokenDTO> saveToken(HttpSession session, @RequestBody String accessToken) {
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setAccessToken(String.valueOf(accessToken));
        session.setAttribute("token", tokenDto.getAccessToken());
        session.setMaxInactiveInterval(5000);
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/payments")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<PaymentDTO>> getPayments(HttpSession session) throws JsonProcessingException {
        String url = "https://api.portone.io/payments";

        String tokenJson = (String) session.getAttribute("token");
        if (tokenJson == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TokenDTO tokenDto = objectMapper.readValue(tokenJson, TokenDTO.class);

        String accessToken = tokenDto.getAccessToken();
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PaymentResponseDTO> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,
                    new ParameterizedTypeReference<PaymentResponseDTO>() {});
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        List<PaymentDTO> payments = Objects.requireNonNull(response.getBody()).getPayments();
        if (payments == null || payments.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/new_payment")
    public ResponseEntity<PaymentIdDTO> savePaymentId(@RequestBody PaymentIdDTO dto) {
        int saved = reservationService.paymentIdSave(dto);
        if (saved == 1) {
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/paymentIdVOList")
    public ResponseEntity<ArrayList<PaymentListDTO>> getPaymentList(
            @RequestParam("paymentId") String paymentId,
            @AuthenticationPrincipal MyUserDetail userDetail) {
        if (userDetail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ArrayList<PaymentListDTO> list = reservationService.PaymentIdList(paymentId, userDetail.getUsername());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/movie_payment_paymentId")
    public ResponseEntity<PaymentDTO> savePaymentDetail(@RequestBody PaymentDTO dto) {
        int saved = reservationService.movie_payment_paymentId(dto);
        if (saved == 1) {
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
