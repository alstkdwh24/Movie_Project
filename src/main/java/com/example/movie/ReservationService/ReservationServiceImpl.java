package com.example.movie.ReservationService;

import com.example.movie.SecuritySet.MyUserDetail;
import com.example.movie.dto.*;
import com.example.movie.entity.*;
import com.example.movie.util.Criteria;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("reservationService")
public class ReservationServiceImpl implements ReservationService {

    private static final String PORTONE_PAYMENT_URL = "https://api.portone.io/payments/{paymentId}";
    private static final int TIMEOUT_MS = 5000;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Override
    public ArrayList<CategoryDTO> getMovieCategory() {
        return reservationMapper.getMovieCategory().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<CategoryDTO> getMovieCategory2() {
        return reservationMapper.getMovieCategory2().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<CategoryDTO> getMovieCategory3() {
        return reservationMapper.getMovieCategory3().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<CategoryDTO> getMovieCategoryChild(CategoryDTO dto) {
        return reservationMapper.getMovieCategoryChild(dto.toEntity()).stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int getReservation_board(Criteria cri) {
        return reservationMapper.getReservation_board(cri);
    }

    @Override
    public int movie_report_resist(ReservationDTO dto) {
        return reservationMapper.movie_report_resist(dto.toEntity());
    }

    @Override
    public List<ReservationDTO> getReservation_pay(Criteria cri) {
        return reservationMapper.getReservation_pay(cri).stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void reservation_Delete(ReservationDTO dto) {
        reservationMapper.reservation_Delete(dto.toEntity());
    }

    @Override
    public ResponseEntity<?> payment_loading(PaymentDTO dto, MyUserDetail userDetail, HttpSession session) {
        if (userDetail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        if (!userDetail.getUsername().equals(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("주문 사용자와 로그인 사용자가 일치하지 않습니다.");
        }

        try {
            String tokenJson = (String) session.getAttribute("token");
            PaymentDTO result = completePayment(dto, userDetail.getUsername(), tokenJson);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentDTO completePayment(PaymentDTO dto, String username, String tokenJson) {
        String paymentId = checkPaymentId(dto.getPaymentId());

        checkDuplicate(paymentId);
        ReservationEntity reservation = getReservation(username, paymentId);
        int price = parsePrice(reservation);

        Map<String, Object> payment = getPortOnePayment(paymentId, tokenJson);
        checkPaid(payment);
        checkPrice(payment, price);

        dto.setUsername(username);
        dto.setReservation_price(String.valueOf(price));
        savePayment(dto);

        return dto;
    }

    private String checkPaymentId(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("결제 번호(paymentId)가 없습니다.");
        }
        return paymentId;
    }

    private void checkDuplicate(String paymentId) {
        if (reservationMapper.existsPaymentById(paymentId) > 0) {
            throw new IllegalStateException("이미 처리된 결제입니다: " + paymentId);
        }
    }

    private ReservationEntity getReservation(String username, String paymentId) {
        ReservationEntity reservation = reservationMapper.paymentId_same(username, paymentId);
        if (reservation == null) {
            throw new IllegalArgumentException("해당 사용자의 예약 주문 내역이 존재하지 않습니다.");
        }
        return reservation;
    }

    private int parsePrice(ReservationEntity reservation) {
        String priceStr = reservation.getReservation_price() != null
                ? reservation.getReservation_price().replaceAll("[^0-9]", "")
                : "";
        if (priceStr.isEmpty()) {
            throw new IllegalArgumentException("서버 예약 금액 정보가 유효하지 않습니다.");
        }
        return Integer.parseInt(priceStr);
    }

    private void checkPaid(Map<String, Object> payment) {
        String status = payment != null ? (String) payment.get("status") : null;
        if (!"PAID".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("결제가 완료되지 않은 상태입니다. status=" + status);
        }
    }

    private void checkPrice(Map<String, Object> payment, int price) {
        Object amount = payment.get("amount");
        if (!(amount instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("PortOne 결제 금액 정보를 확인할 수 없습니다.");
        }
        Object total = ((Map<?, ?>) amount).get("total");
        if (!(total instanceof Number)) {
            throw new IllegalArgumentException("PortOne 결제 총액(total) 정보를 확인할 수 없습니다.");
        }

        int paidPrice = ((Number) total).intValue();
        if (paidPrice != price) {
            throw new IllegalArgumentException(
                    "결제 금액이 서버 주문 금액과 일치하지 않습니다. expected=" + price + ", actual=" + paidPrice);
        }
    }

    private void savePayment(PaymentDTO dto) {
        if (reservationMapper.paymentVO(dto.toEntity()) != 1) {
            throw new IllegalStateException("결제 내역 저장에 실패했습니다.");
        }
    }

    private Map<String, Object> getPortOnePayment(String paymentId, String tokenJson) {
        String token = parseToken(tokenJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(
                    PORTONE_PAYMENT_URL,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {},
                    paymentId
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new IllegalArgumentException("PortOne 결제 단건 조회에 실패했습니다.", e);
        }
    }

    private String parseToken(String tokenJson) {
        if (tokenJson == null || tokenJson.isBlank()) {
            throw new IllegalArgumentException("결제 검증용 인증 토큰이 없습니다.");
        }
        String token = tokenJson.trim();
        if (token.startsWith("{")) {
            try {
                TokenDTO tokenDto = OBJECT_MAPPER.readValue(token, TokenDTO.class);
                token = tokenDto.getAccessToken();
            } catch (Exception e) {
                throw new IllegalArgumentException("결제 검증용 토큰 파싱에 실패했습니다.", e);
            }
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("유효한 결제 검증용 accessToken이 없습니다.");
        }
        return token;
    }

    private RestTemplate getRestTemplate() {
        if (this.restTemplate != null) {
            return this.restTemplate;
        }
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT_MS);
        factory.setReadTimeout(TIMEOUT_MS);
        return new RestTemplate(factory);
    }

    @Override
    public ArrayList<PaymentDTO> getPayment_report(String username) {
        return reservationMapper.getPayment_report(username).stream()
                .map(PaymentDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ReservationDTO paymentId_same(String username, String paymentId) {
        return ReservationDTO.fromEntity(reservationMapper.paymentId_same(username, paymentId));
    }

    @Override
    public int paymentIdSave(PaymentIdDTO dto) {
        return reservationMapper.paymentIdSave(dto.toEntity());
    }

    @Override
    public ArrayList<PaymentListDTO> PaymentIdList(String paymentId, String username) {
        return reservationMapper.PaymentIdList(paymentId, username).stream()
                .map(PaymentListDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int movie_payment_paymentId(PaymentDTO dto) {
        return reservationMapper.movie_payment_paymentId(dto.toEntity());
    }
}
