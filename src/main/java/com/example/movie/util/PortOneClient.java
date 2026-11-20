package com.example.movie.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 포트원 V2 결제 단건 조회.
 * API Secret은 서버 설정(portone.api-secret)에서만 읽는다. 브라우저로 보내지 않는다.
 */
@Component
public class PortOneClient {

    private static final String PAYMENT_URL = "https://api.portone.io/payments/{paymentId}";

    private final RestTemplate restTemplate;
    private final String apiSecret;

    public PortOneClient(RestTemplate restTemplate, @Value("${portone.api-secret:}") String apiSecret) {
        this.restTemplate = restTemplate;
        this.apiSecret = apiSecret;
    }

    /** 결제가 없으면 null. 그 밖의 통신 오류는 RestClientException으로 올린다. */
    public PortOnePayment getPayment(String paymentId) {
        if (apiSecret.isBlank()) {
            throw new IllegalStateException("portone.api-secret 설정이 없습니다");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "PortOne " + apiSecret);
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

    public record PortOnePayment(String status, long totalAmount) {
        public boolean isPaid() {
            return "PAID".equals(status);
        }
    }
}
