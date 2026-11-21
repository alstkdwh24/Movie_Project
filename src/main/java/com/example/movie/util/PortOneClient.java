package com.example.movie.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 포트원 V2 결제 단건 조회와 결제 취소.
 * API Secret은 서버 설정(portone.api-secret)에서만 읽는다. 브라우저로 보내지 않는다.
 */
@Component
public class PortOneClient {

    private static final String PAYMENT_URL = "https://api.portone.io/payments/{paymentId}";
    private static final String CANCEL_URL = "https://api.portone.io/payments/{paymentId}/cancel";

    private final RestTemplate restTemplate;
    private final String apiSecret;

    public PortOneClient(RestTemplate restTemplate, @Value("${portone.api-secret:}") String apiSecret) {
        this.restTemplate = restTemplate;
        this.apiSecret = apiSecret;
    }

    /** 결제가 없으면 null. 그 밖의 통신 오류는 RestClientException으로 올린다. */
    public PortOnePayment getPayment(String paymentId) {
        checkPaymentId(paymentId);
        HttpHeaders headers = authHeaders();
        try {
            JsonNode body = restTemplate.exchange(PAYMENT_URL, HttpMethod.GET, new HttpEntity<>(headers),
                    JsonNode.class, paymentId).getBody();
            if (body == null) {
                throw new IllegalStateException("포트원 결제 조회 응답이 비어 있습니다");
            }
            return new PortOnePayment(body.path("status").asText(), body.path("amount").path("total").asLong(-1));
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    /** 결제 전액 취소(amount를 보내지 않으면 전액, reason은 필수). 실패하면 RestClientException을 올린다. */
    public void cancelPayment(String paymentId, String reason) {
        checkPaymentId(paymentId);
        HttpHeaders headers = authHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.exchange(CANCEL_URL, HttpMethod.POST, new HttpEntity<>(Map.of("reason", reason), headers),
                JsonNode.class, paymentId);
    }

    /** 경로 변수로 들어가는 값이라 / . 같은 문자가 섞이면 다른 결제 경로로 바뀔 수 있다 */
    private static void checkPaymentId(String paymentId) {
        if (paymentId == null || !paymentId.matches("[A-Za-z0-9_-]{1,64}")) {
            throw new IllegalArgumentException("paymentId 형식이 올바르지 않습니다");
        }
    }

    private HttpHeaders authHeaders() {
        if (apiSecret.isBlank()) {
            throw new IllegalStateException("portone.api-secret 설정이 없습니다");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "PortOne " + apiSecret);
        return headers;
    }

    public record PortOnePayment(String status, long totalAmount) {
        public boolean isPaid() {
            return "PAID".equals(status);
        }
    }
}
