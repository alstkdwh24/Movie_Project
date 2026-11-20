package com.example.movie;

import com.example.movie.ReservationService.ReservationMapper;
import com.example.movie.ReservationService.ReservationServiceImpl;
import com.example.movie.RestController.RestCategoryController;
import com.example.movie.RestController.RestPaymentController;
import com.example.movie.RestController.RestReservation;
import com.example.movie.commandVO.PaymentVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.util.PortOneClient;
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
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 예약 삭제 본인 확인, 결제 금액 서버 검증, 미결제 예약 만료를 H2(MySQL 모드)와 실제 매퍼 XML로 확인한다.
 * 포트원 조회(PortOneClient)만 목으로 바꾼다. HTTP·스프링 컨텍스트는 거치지 않는다.
 */
class ReservationPaymentSafetyTest {

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
                  movie_Seat varchar(20), username varchar(50), reservation_price varchar(20), paymentId varchar(100),
                  status varchar(10) not null default 'PENDING',
                  reserved_at datetime not null default current_timestamp,
                  constraint uq_reservation_seat unique (movie_title, movie_place, movie_time, movie_Seat),
                  constraint uq_reservation_paymentId unique (paymentId)
                )""");
        jdbc.execute("""
                create table movie_payment (
                  username varchar(50), movie_title varchar(100), movie_time varchar(50), movie_place varchar(100),
                  paymentId varchar(100), reservation_price varchar(20), movie_Seat varchar(20), payment_time datetime,
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
        paymentApi = new RestPaymentController(new RestTemplate());
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
    void 포트원_결제_금액이_예약_금액과_다르면_409이고_저장하지_않는다() {
        insertReservation("alice", "pay-1", "PENDING", 0);
        when(portOne.getPayment("pay-1")).thenReturn(new PortOnePayment("PAID", 100));

        assertThat(complete("pay-1", "alice")).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rows("movie_payment")).isZero();
        assertThat(statusOf("pay-1")).isEqualTo("PENDING");
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

        // 같은 결제를 다시 확정해도 결제 행이 늘지 않는다
        assertThat(complete("pay-1", "alice")).isEqualTo(HttpStatus.NOT_FOUND);
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

    // ---- 미결제 만료 ----

    @Test
    void 기준_시간이_지난_결제_전_예약만_만료된다() {
        insertReservation("alice", "old-pending", "PENDING", 11);
        insertReservation("alice", "new-pending", "PENDING", 5);
        insertReservation("alice", "old-paid", "PAID", 60);

        int deleted = service.expirePendingReservations(10);

        assertThat(deleted).isEqualTo(1);
        assertThat(jdbc.queryForList("select paymentId from reservation_contents order by paymentId", String.class))
                .containsExactly("new-pending", "old-paid");
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
