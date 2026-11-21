package com.example.movie;

import com.example.movie.ReservationService.ReservationMapper;
import com.example.movie.ReservationService.ReservationServiceImpl;
import com.example.movie.ReservationService.ReservationService.ExpiryResult;
import com.example.movie.RestController.RestCategoryController;
import com.example.movie.RestController.RestPaymentController;
import com.example.movie.RestController.RestReservation;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.PortOneClient;
import com.example.movie.util.PortOneWebhookVerifier;
import com.example.movie.util.PortOneWebhookVerifierTest;
import com.example.movie.util.PortOneClient.PortOnePayment;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClientException;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 예약 삭제 본인 확인, 결제 금액 서버 검증, 미결제 예약 만료를 H2(MySQL 모드)와 실제 매퍼 XML로 확인한다.
 * 포트원 조회(PortOneClient)만 목으로 바꾼다. HTTP·스프링 컨텍스트는 거치지 않는다.
 */
class ReservationPaymentSafetyTest {

    // 서버가 만드는 형식(rsv- + UUID). 자동 환불은 이 형식에만 한다
    private static final String RSV_MISMATCH = "rsv-00000000-0000-4000-8000-000000000001";
    private static final String RSV_GONE = "rsv-00000000-0000-4000-8000-000000000002";
    private static final String RSV_USED = "rsv-00000000-0000-4000-8000-000000000003";
    private static final String RSV_WEBHOOK = "rsv-00000000-0000-4000-8000-000000000004";

    private JdbcTemplate jdbc;
    private PortOneClient portOne;
    private RestReservation reservationApi;
    private RestPaymentController paymentApi;
    private RestCategoryController categoryApi;
    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:pay" + System.nanoTime() + ";MODE=MySQL;DB_CLOSE_DELAY=-1");
        jdbc = new JdbcTemplate(ds);
        jdbc.execute("""
                create table reservation_contents (
                  reservation_number int auto_increment primary key,
                  movie_title varchar(100), movie_place varchar(100), movie_time varchar(50),
                  movie_Seat varchar(100), username varchar(50), reservation_price varchar(20), paymentId varchar(100),
                  status varchar(10) not null default 'PENDING',
                  reserved_at datetime not null default current_timestamp,
                  constraint uq_reservation_seat unique (movie_title, movie_place, movie_time, movie_Seat),
                  constraint uq_reservation_paymentId unique (paymentId)
                )""");
        jdbc.execute("""
                create table movie_payment (
                  username varchar(50), movie_title varchar(100), movie_time varchar(50), movie_place varchar(100),
                  paymentId varchar(100), reservation_price varchar(20), movie_Seat varchar(100), payment_time datetime,
                  constraint uq_movie_payment_paymentId unique (paymentId)
                )""");

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ds);
        factoryBean.setMapperLocations(new ClassPathResource("templates/movie/mappers/ReservationMapper.xml"));
        factoryBean.setTypeAliasesPackage("com.example.movie.commandVO");
        ReservationMapper mapper = new SqlSessionTemplate(factoryBean.getObject()).getMapper(ReservationMapper.class);

        portOne = mock(PortOneClient.class);
        service = new ReservationServiceImpl();
        ReflectionTestUtils.setField(service, "reservationMapper", mapper);
        ReflectionTestUtils.setField(service, "portOneClient", portOne);
        ReflectionTestUtils.setField(service, "transactionTemplate",
                new TransactionTemplate(new DataSourceTransactionManager(ds)));

        reservationApi = new RestReservation();
        ReflectionTestUtils.setField(reservationApi, "reservationService", service);
        paymentApi = new RestPaymentController();
        ReflectionTestUtils.setField(paymentApi, "webhookVerifier", new PortOneWebhookVerifier(PortOneWebhookVerifierTest.SECRET));
        ReflectionTestUtils.setField(paymentApi, "reservationService", service);
        categoryApi = new RestCategoryController();
        ReflectionTestUtils.setField(categoryApi, "reservationService", service);
        ReflectionTestUtils.setField(categoryApi, "seatPrice", 1000);
    }

    // ---- 예약 등록 ----

    @Test
    void 예약_등록은_클라이언트가_보낸_사용자와_금액을_무시한다() {
        ReservationVO dto = ReservationVO.builder().movieTitle("영화").moviePlace("강남").movieTime("10:00")
                .movieSeat("A1").username("mallory").reservation_price("1").paymentId("pay-1").build();

        HttpStatus status = HttpStatus.valueOf(categoryApi.createReservation(dto, user("alice")).getStatusCode().value());

        assertThat(status).isEqualTo(HttpStatus.CREATED);
        assertThat(jdbc.queryForMap("select username, reservation_price from reservation_contents"))
                .containsEntry("USERNAME", "alice").containsEntry("RESERVATION_PRICE", "1000");
        // paymentId도 서버가 만든다(이미 결제된 paymentId를 끼워 넣지 못하게)
        assertThat(jdbc.queryForObject("select paymentId from reservation_contents", String.class))
                .startsWith("rsv-").isNotEqualTo("pay-1");
    }

    // ---- 예약 삭제 ----

    @Test
    void 남의_예약은_지울_수_없고_본인_예약은_지워진다() {
        int number = insertReservation("alice", "pay-1", "PENDING", 0);

        assertThat(delete(number, "bob")).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rows("reservation_contents")).isEqualTo(1);

        assertThat(delete(number, "alice")).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(rows("reservation_contents")).isZero();
    }

    @Test
    void 결제가_끝난_예약은_취소로_지워지지_않는다() {
        int number = insertReservation("alice", "pay-1", "PAID", 0);

        assertThat(delete(number, "alice")).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(rows("reservation_contents")).isEqualTo(1);
    }

    // ---- 결제 확정 ----

    @Test
    void 포트원_결제_금액이_예약_금액과_다르면_환불하고_410_저장하지_않는다() {
        insertReservation("alice", RSV_MISMATCH, "PENDING", 0);
        when(portOne.getPayment(RSV_MISMATCH)).thenReturn(new PortOnePayment("PAID", 100));

        assertThat(complete(RSV_MISMATCH, "alice")).isEqualTo(HttpStatus.GONE);
        verify(portOne).cancelPayment(eq(RSV_MISMATCH), anyString());
        assertThat(rows("movie_payment")).isZero();
        assertThat(statusOf(RSV_MISMATCH)).isEqualTo("PENDING");
    }

    // ---- 자동 환불 ----

    @Test
    void 결제창에_있는_사이_예약이_만료로_지워졌으면_자동_환불하고_410() {
        // 예약 행은 이미 만료 작업이 지웠고, 포트원에는 결제가 완료돼 있다
        when(portOne.getPayment(RSV_GONE)).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(complete(RSV_GONE, "alice")).isEqualTo(HttpStatus.GONE);
        verify(portOne).cancelPayment(eq(RSV_GONE), anyString());
        assertThat(rows("movie_payment")).isZero();
    }

    @Test
    void 이미_확정에_쓰인_결제와_서버가_만들지_않은_예전_결제는_환불하지_않는다() {
        jdbc.update("insert into movie_payment(username, paymentId, reservation_price) values ('alice', ?, '1000')", RSV_USED);
        when(portOne.getPayment(anyString())).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(complete(RSV_USED, "bob")).isEqualTo(HttpStatus.CONFLICT);
        assertThat(complete("12345678", "bob")).isEqualTo(HttpStatus.NOT_FOUND);
        verify(portOne, never()).cancelPayment(anyString(), anyString());
    }

    @Test
    void paymentId에_경로_문자가_있으면_400이고_포트원을_부르지_않는다() {
        assertThat(complete("rsv-x/../" + RSV_USED, "alice")).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(portOne);
    }

    @Test
    void 자동_환불이_실패하면_502() {
        when(portOne.getPayment(RSV_GONE)).thenReturn(new PortOnePayment("PAID", 1000));
        doThrow(new RestClientException("timeout")).when(portOne).cancelPayment(eq(RSV_GONE), anyString());

        assertThat(complete(RSV_GONE, "alice")).isEqualTo(HttpStatus.BAD_GATEWAY);
    }

    @Test
    void 포트원에서_결제가_PAID가_아니면_409() {
        insertReservation("alice", "pay-1", "PENDING", 0);
        when(portOne.getPayment("pay-1")).thenReturn(new PortOnePayment("READY", 1000));

        assertThat(complete("pay-1", "alice")).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rows("movie_payment")).isZero();
    }

    @Test
    void 결제와_금액이_맞으면_201이고_예약은_PAID_결제는_DB_금액으로_저장() {
        insertReservation("alice", "pay-1", "PENDING", 0);
        when(portOne.getPayment("pay-1")).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(complete("pay-1", "alice")).isEqualTo(HttpStatus.CREATED);
        assertThat(statusOf("pay-1")).isEqualTo("PAID");
        assertThat(jdbc.queryForMap("select username, reservation_price, movie_Seat from movie_payment"))
                .containsEntry("USERNAME", "alice").containsEntry("RESERVATION_PRICE", "1000")
                .containsEntry("MOVIE_SEAT", "A1");

        // 같은 결제를 다시 확정하면 이미 확정됐다고 201로 답하고, 결제 행은 늘지 않는다
        assertThat(complete("pay-1", "alice")).isEqualTo(HttpStatus.CREATED);
        assertThat(rows("movie_payment")).isEqualTo(1);
    }

    @Test
    void 이미_다른_예약에_쓴_결제로는_확정되지_않고_예약은_PENDING으로_남는다() {
        // 예전 데이터처럼 이미 결제 기록이 있는 paymentId로 새 예약이 남아 있는 경우
        insertReservation("bob", "pay-2", "PENDING", 0);
        jdbc.update("insert into movie_payment(username, paymentId, reservation_price) values ('alice', 'pay-2', '1000')");
        when(portOne.getPayment("pay-2")).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(complete("pay-2", "bob")).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rows("movie_payment")).isEqualTo(1);
        assertThat(statusOf("pay-2")).isEqualTo("PENDING"); // markPaid도 함께 롤백됐는지
    }

    @Test
    void 남의_예약으로_결제_확정하면_404이고_포트원을_부르지_않는다() {
        insertReservation("alice", "pay-1", "PENDING", 0);

        assertThat(complete("pay-1", "bob")).isEqualTo(HttpStatus.NOT_FOUND);
        verify(portOne, never()).getPayment(anyString());
    }

    @Test
    void 로그인하지_않으면_결제_확정은_401() {
        PaymentVO vo = new PaymentVO();
        vo.setPaymentId("pay-1");
        assertThat(paymentApi.completePayment(vo, null).getStatusCode().value()).isEqualTo(401);
    }

    // ---- 웹훅 ----

    @Test
    void 결제_후_창을_닫아도_웹훅으로_확정되고_늦게_온_브라우저_요청도_성공() throws Exception {
        insertReservation("alice", RSV_WEBHOOK, "PENDING", 0);
        when(portOne.getPayment(RSV_WEBHOOK)).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(webhook("Transaction.Paid", RSV_WEBHOOK, true)).isEqualTo(HttpStatus.OK);
        assertThat(statusOf(RSV_WEBHOOK)).isEqualTo("PAID");
        assertThat(rows("movie_payment")).isEqualTo(1);

        assertThat(complete(RSV_WEBHOOK, "alice")).isEqualTo(HttpStatus.CREATED);
        assertThat(webhook("Transaction.Paid", RSV_WEBHOOK, true)).isEqualTo(HttpStatus.OK); // 재전송돼도 그대로
        assertThat(rows("movie_payment")).isEqualTo(1);
        verify(portOne, never()).cancelPayment(anyString(), anyString());
    }

    @Test
    void 예약이_만료로_지워진_뒤_결제_웹훅이_오면_환불() throws Exception {
        when(portOne.getPayment(RSV_GONE)).thenReturn(new PortOnePayment("PAID", 1000));

        assertThat(webhook("Transaction.Paid", RSV_GONE, true)).isEqualTo(HttpStatus.OK);
        verify(portOne).cancelPayment(eq(RSV_GONE), anyString());
    }

    @Test
    void 웹훅_환불이_실패하면_503으로_재전송을_받는다() throws Exception {
        when(portOne.getPayment(RSV_GONE)).thenReturn(new PortOnePayment("PAID", 1000));
        doThrow(new RestClientException("timeout")).when(portOne).cancelPayment(eq(RSV_GONE), anyString());

        assertThat(webhook("Transaction.Paid", RSV_GONE, true)).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    void 서명이_틀린_웹훅은_401이고_모르는_type은_무시한다() throws Exception {
        insertReservation("alice", RSV_WEBHOOK, "PENDING", 0);

        assertThat(webhook("Transaction.Paid", RSV_WEBHOOK, false)).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(webhook("Transaction.Ready", RSV_WEBHOOK, true)).isEqualTo(HttpStatus.OK);
        verifyNoInteractions(portOne);
        assertThat(statusOf(RSV_WEBHOOK)).isEqualTo("PENDING");
    }

    @Test
    void 웹훅_본문이_64KB를_넘으면_서명_검증_전에_413() throws Exception {
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        request.setContent(new byte[64 * 1024 + 1]);

        assertThat(paymentApi.paymentWebhook(request).getStatusCode().value()).isEqualTo(413);
        verifyNoInteractions(portOne);
    }

    // ---- 결제 내역 ----

    @Test
    void 결제_내역은_로그인한_본인_것만_돌려준다() {
        jdbc.update("insert into movie_payment(username, paymentId, reservation_price) values ('alice', 'pay-a', '1000')");
        jdbc.update("insert into movie_payment(username, paymentId, reservation_price) values ('bob', 'pay-b', '1000')");

        assertThat(paymentApi.payment_report(user("alice")).getBody())
                .extracting(PaymentVO::getPaymentId).containsExactly("pay-a");
        assertThat(paymentApi.payment_report(null).getStatusCode().value()).isEqualTo(401);
    }

    // ---- 미결제 만료 ----

    @Test
    void 기준_시간이_지난_결제_전_예약만_만료된다() {
        insertReservation("alice", "old-pending", "PENDING", 11);
        insertReservation("alice", "new-pending", "PENDING", 5);
        insertReservation("alice", "old-paid", "PAID", 60);

        ExpiryResult result = service.expirePendingReservations(10, 100);

        assertThat(result.released()).isEqualTo(1);
        assertThat(jdbc.queryForList("select paymentId from reservation_contents order by paymentId", String.class))
                .containsExactly("new-pending", "old-paid");
    }

    @Test
    void 만료_작업은_이미_결제된_예약은_지우지_않고_확정하고_조회가_실패하면_남겨_둔다() {
        insertReservation("alice", "rsv-paid", "PENDING", 11);   // 결제 후 창을 닫아 확정 요청이 안 온 예약
        insertReservation("alice", "rsv-unpaid", "PENDING", 11); // 결제하지 않은 예약
        insertReservation("alice", "rsv-error", "PENDING", 11);  // 포트원 조회 실패
        when(portOne.getPayment("rsv-paid")).thenReturn(new PortOnePayment("PAID", 1000));
        when(portOne.getPayment("rsv-unpaid")).thenReturn(null);
        when(portOne.getPayment("rsv-error")).thenThrow(new RestClientException("timeout"));

        ExpiryResult result = service.expirePendingReservations(10, 100);

        assertThat(result).isEqualTo(new ExpiryResult(1, 1, 0, 1));
        assertThat(statusOf("rsv-paid")).isEqualTo("PAID");
        assertThat(rows("movie_payment")).isEqualTo(1);
        assertThat(jdbc.queryForList("select paymentId from reservation_contents order by paymentId", String.class))
                .containsExactly("rsv-error", "rsv-paid");
    }

    // ---- 도우미 ----

    private int insertReservation(String username, String paymentId, String status, int minutesAgo) {
        jdbc.update("""
                insert into reservation_contents(movie_title, movie_place, movie_time, movie_Seat, username,
                  reservation_price, paymentId, status, reserved_at)
                values ('영화', '강남', '10:00', ?, ?, '1000', ?, ?, timestampadd(minute, ?, current_timestamp))""",
                paymentId.equals("pay-1") ? "A1" : paymentId, username, paymentId, status, -minutesAgo);
        return jdbc.queryForObject("select reservation_number from reservation_contents where paymentId=?",
                Integer.class, paymentId);
    }

    private HttpStatus delete(int reservationNumber, String username) {
        ReservationVO vo = new ReservationVO();
        vo.setReservation_number(reservationNumber);
        return HttpStatus.valueOf(reservationApi.reservation_Delete(vo, user(username)).getStatusCode().value());
    }

    private HttpStatus complete(String paymentId, String username) {
        PaymentVO vo = new PaymentVO();
        vo.setPaymentId(paymentId);
        return HttpStatus.valueOf(paymentApi.completePayment(vo, user(username)).getStatusCode().value());
    }

    private HttpStatus webhook(String type, String paymentId, boolean validSignature) throws Exception {
        String body = """
                {"type":"%s","timestamp":"2026-11-21T00:00:00Z","data":{"storeId":"store-test","paymentId":"%s"}}"""
                .formatted(type, paymentId);
        String id = "msg_" + System.nanoTime();
        String ts = String.valueOf(java.time.Instant.now().getEpochSecond());
        String signature = PortOneWebhookVerifierTest.sign(id, ts, validSignature ? body : body + " ");
        return HttpStatus.valueOf(paymentApi.paymentWebhook(body, id, ts, signature).getStatusCode().value());
    }

    private String statusOf(String paymentId) {
        return jdbc.queryForObject("select status from reservation_contents where paymentId=?", String.class, paymentId);
    }

    private int rows(String table) {
        return jdbc.queryForObject("select count(*) from " + table, Integer.class);
    }

    private static Principal user(String name) {
        return () -> name;
    }
}
