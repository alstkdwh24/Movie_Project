package com.example.movie;

import com.example.movie.RestController.RestCategoryController;
import com.example.movie.ReservationService.ReservationMapper;
import com.example.movie.ReservationService.ReservationServiceImpl;
import com.example.movie.commandVO.ReservationVO;
import org.apache.ibatis.session.SqlSessionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 같은 좌석에 예매 요청 10건이 동시에 들어와도 1건만 저장되고 나머지는 409인지 확인한다.
 * 운영 MySQL 대신 H2 메모리 DB(MySQL 모드)에 운영과 같은 유니크 제약(uq_reservation_seat)을 만들고,
 * 실제 매퍼 XML → 서비스 → 컨트롤러 경로를 그대로 쓴다.
 */
class SeatDuplicateConcurrencyTest {

    private static final int REQUESTS = 10;

    private JdbcTemplate jdbc;
    private RestCategoryController controller;

    @BeforeEach
    void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:seat" + System.nanoTime() + ";MODE=MySQL;DB_CLOSE_DELAY=-1");
        jdbc = new JdbcTemplate(ds);
        jdbc.execute("""
                create table reservation_contents (
                  reservation_number int auto_increment primary key,
                  movie_title varchar(100), movie_place varchar(100), movie_time varchar(50),
                  movie_Seat varchar(20), username varchar(50), reservation_price varchar(20), paymentId varchar(100),
                  constraint uq_reservation_seat unique (movie_title, movie_place, movie_time, movie_Seat)
                )""");

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ds);
        factoryBean.setMapperLocations(new ClassPathResource("templates/movie/mappers/ReservationMapper.xml"));
        factoryBean.setTypeAliasesPackage("com.example.movie.commandVO"); // 매퍼 XML이 ReservationVO 등 짧은 이름을 쓴다
        SqlSessionFactory factory = factoryBean.getObject();
        // SqlSessionTemplate이 DB 오류를 스프링 예외(DuplicateKeyException)로 바꿔 준다(운영과 같은 경로)
        ReservationMapper mapper = new SqlSessionTemplate(factory).getMapper(ReservationMapper.class);

        ReservationServiceImpl service = new ReservationServiceImpl();
        ReflectionTestUtils.setField(service, "reservationMapper", mapper);
        controller = new RestCategoryController();
        ReflectionTestUtils.setField(controller, "reservationService", service);
    }

    @Test
    void 같은_좌석_동시_요청_10건이면_1건만_저장되고_9건은_409() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(REQUESTS);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<HttpStatus>> results = new ArrayList<>();
        for (int i = 0; i < REQUESTS; i++) {
            ReservationVO dto = seat("user" + i, "pay-" + i, "A1");
            results.add(pool.submit(() -> {
                start.await();
                return HttpStatus.valueOf(controller.createReservation(dto).getStatusCode().value());
            }));
        }
        start.countDown();

        int created = 0, conflict = 0;
        for (Future<HttpStatus> f : results) {
            HttpStatus status = f.get(10, TimeUnit.SECONDS);
            if (status == HttpStatus.CREATED) created++;
            else if (status == HttpStatus.CONFLICT) conflict++;
        }
        pool.shutdown();

        System.out.printf("[좌석 동시 예매] 요청 %d건 → 201 %d건, 409 %d건, 저장 %d행%n",
                REQUESTS, created, conflict, countRows());
        assertThat(created).isEqualTo(1);
        assertThat(conflict).isEqualTo(REQUESTS - 1);
        assertThat(countRows()).isEqualTo(1);
    }

    @Test
    void 좌석이_다르면_모두_저장된다() {
        for (int i = 0; i < REQUESTS; i++) {
            assertThat(controller.createReservation(seat("user" + i, "pay-" + i, "B" + i)).getStatusCode())
                    .isEqualTo(HttpStatus.CREATED);
        }
        assertThat(countRows()).isEqualTo(REQUESTS);
    }

    private ReservationVO seat(String username, String paymentId, String seat) {
        ReservationVO dto = new ReservationVO();
        dto.setMovieTitle("테스트 영화");
        dto.setMoviePlace("강남점");
        dto.setMovieTime("2026-11-20 19:00");
        dto.setMovieSeat(seat);
        dto.setUsername(username);
        dto.setReservation_price("12000");
        dto.setPaymentId(paymentId);
        return dto;
    }

    private int countRows() {
        return jdbc.queryForObject("select count(*) from reservation_contents", Integer.class);
    }
}
