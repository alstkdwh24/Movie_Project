package com.example.movie;

import com.example.movie.util.PortOneClient;
import com.example.movie.util.PortOneClient.PortOnePayment;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/** 포트원 V2 결제 단건 조회 요청 형식(주소·인증 헤더)과 응답 해석을 확인한다. 실제 포트원에는 요청하지 않는다. */
class PortOneClientTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();

    @Test
    void 결제_상태와_총액을_읽는다() {
        server.expect(requestTo("https://api.portone.io/payments/pay-1"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "PortOne test-secret"))
                .andRespond(withSuccess("{\"status\":\"PAID\",\"amount\":{\"total\":1000,\"paid\":1000}}",
                        MediaType.APPLICATION_JSON));

        PortOnePayment payment = new PortOneClient(restTemplate, "test-secret").getPayment("pay-1");

        assertThat(payment.isPaid()).isTrue();
        assertThat(payment.totalAmount()).isEqualTo(1000);
        server.verify();
    }

    @Test
    void 결제가_없으면_null() {
        server.expect(requestTo("https://api.portone.io/payments/none")).andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThat(new PortOneClient(restTemplate, "test-secret").getPayment("none")).isNull();
    }

    @Test
    void 결제_취소는_cancel_경로에_취소_사유만_보내_전액_취소한다() {
        server.expect(requestTo("https://api.portone.io/payments/pay-1/cancel"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "PortOne test-secret"))
                .andExpect(content().json("{\"reason\":\"예약 시간이 지나 좌석이 해제됨\"}", true))
                .andRespond(withSuccess("{\"cancellation\":{\"status\":\"SUCCEEDED\"}}", MediaType.APPLICATION_JSON));

        new PortOneClient(restTemplate, "test-secret").cancelPayment("pay-1", "예약 시간이 지나 좌석이 해제됨");

        server.verify();
    }

    @Test
    void Secret_설정이_없으면_요청하지_않고_예외() {
        assertThatThrownBy(() -> new PortOneClient(restTemplate, "").getPayment("pay-1"))
                .isInstanceOf(IllegalStateException.class);
        server.verify(); // 요청이 하나도 없어야 한다
    }
}
