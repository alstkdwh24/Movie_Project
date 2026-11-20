package com.example.movie.ReservationService;

import com.example.movie.commandVO.*;
import com.example.movie.util.Criteria;
import com.example.movie.util.PortOneClient;
import com.example.movie.util.PortOneClient.PortOnePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("reservationService")
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private PortOneClient portOneClient;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public ArrayList<CategoryVO> getMovieCategory() {
        return reservationMapper.getMovieCategory();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategory2() {
        return reservationMapper.getMovieCategory2();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategory3() {
        return reservationMapper.getMovieCategory3();
    }

    @Override
    public ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo) {
        return reservationMapper.getMovieCategoryChild(vo);
    }

    @Override
    public int getReservation_board(Criteria cri) {




        return reservationMapper.getReservation_board(cri);
    }

    @Override
    public int movie_report_resist(ReservationVO vo) {
        return reservationMapper.movie_report_resist(vo);
    }

    @Override
    public List<ReservationVO> getReservation_pay(Criteria cri) {


        return reservationMapper.getReservation_pay(cri);
    }

    @Override
    public int reservation_Delete(ReservationVO vo) {
        return reservationMapper.reservation_Delete(vo);
    }

    @Override
    public PaymentCompleteResult completePayment(String username, String paymentId) {
        ReservationVO reservation = reservationMapper.findPendingForPayment(username, paymentId);
        if (reservation == null) {
            return PaymentCompleteResult.RESERVATION_NOT_FOUND;
        }

        // 외부 호출은 트랜잭션 밖에서 한다(포트원 응답을 기다리는 동안 DB 커넥션을 잡지 않도록)
        PortOnePayment payment = portOneClient.getPayment(paymentId);
        if (payment == null || !payment.isPaid()) {
            return PaymentCompleteResult.NOT_PAID;
        }
        if (!sameAmount(payment.totalAmount(), reservation.getReservation_price())) {
            log.warn("결제 금액 불일치 paymentId={} 포트원={} 예약={}", paymentId, payment.totalAmount(), reservation.getReservation_price());
            return PaymentCompleteResult.AMOUNT_MISMATCH;
        }

        try {
            return transactionTemplate.execute(status -> confirmPaid(username, paymentId, reservation));
        } catch (DuplicateKeyException e) {
            // movie_payment.paymentId 유니크 위반 = 이미 다른 예약 확정에 쓴 결제. 트랜잭션은 롤백된다
            log.warn("이미 사용된 결제로 확정 시도 paymentId={}", paymentId);
            return PaymentCompleteResult.NOT_PENDING;
        }
    }

    private PaymentCompleteResult confirmPaid(String username, String paymentId, ReservationVO reservation) {
        if (reservationMapper.markPaid(reservation.getReservation_number()) != 1) {
            // 결제는 됐는데 예약이 만료·중복 처리된 경우. 환불이 필요할 수 있어 남긴다
            log.warn("결제 확인 후 예약 확정 실패 paymentId={} reservation_number={}", paymentId, reservation.getReservation_number());
            return PaymentCompleteResult.NOT_PENDING;
        }
        // 금액·좌석은 클라이언트 값이 아니라 DB 예약 값으로 저장한다
        reservationMapper.paymentVO(PaymentVO.builder()
                .paymentId(paymentId)
                .username(username)
                .movie_title(reservation.getMovie_title())
                .movie_place(reservation.getMovie_place())
                .movie_time(reservation.getMovie_time())
                .movie_Seat(reservation.getMovie_Seat())
                .reservation_price(reservation.getReservation_price())
                .build());
        return PaymentCompleteResult.COMPLETED;
    }

    private boolean sameAmount(long paidAmount, String reservationPrice) {
        try {
            return paidAmount == Long.parseLong(reservationPrice.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public int expirePendingReservations(int minutes) {
        return reservationMapper.deleteExpiredPending(minutes);
    }

    @Override
    public int paymentVO(PaymentVO vo) {
        return reservationMapper.paymentVO(vo);
    }

    @Override
    public ArrayList<PaymentVO> getPayment_report(String username) {
        return reservationMapper.getPayment_report(username);
    }

    @Override
    public ReservationVO paymentId_same(String username, String paymentId) {
        return reservationMapper.paymentId_same(username,paymentId);
    }

    @Override
    public int paymentIdSave(PaymentIdVO vo) {
        return reservationMapper.paymentIdSave(vo);
    }

    @Override
    public int movie_payment_paymentId(PaymentVO vo) {
        return reservationMapper.movie_payment_paymentId(vo);
    }


}
