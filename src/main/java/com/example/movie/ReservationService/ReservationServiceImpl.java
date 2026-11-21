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
            if (reservationMapper.countPaidForUser(username, paymentId) > 0) {
                // 웹훅이 먼저 확정한 경우. 브라우저 요청에도 성공으로 답한다
                return PaymentCompleteResult.COMPLETED;
            }
            // 결제창에 있는 사이 예약이 만료로 지워졌다면, 좌석 없이 돈만 빠진 결제라 환불한다
            return refundIfOrphan(paymentId);
        }

        // 외부 호출은 트랜잭션 밖에서 한다(포트원 응답을 기다리는 동안 DB 커넥션을 잡지 않도록)
        PortOnePayment payment = portOneClient.getPayment(paymentId);
        if (payment == null || !payment.isPaid()) {
            return PaymentCompleteResult.NOT_PAID;
        }
        if (!sameAmount(payment.totalAmount(), reservation.getReservation_price())) {
            log.warn("결제 금액 불일치 paymentId={} 포트원={} 예약={}", paymentId, payment.totalAmount(), reservation.getReservation_price());
            return refundUnrecorded(paymentId, "결제 금액이 예약 금액과 다름");
        }

        PaymentCompleteResult result = confirmInTransaction(username, paymentId, reservation);
        if (result == PaymentCompleteResult.NOT_PENDING
                && reservationMapper.countReservationsByPaymentId(paymentId) == 0) {
            // 확인하는 사이 만료 작업이 예약을 지운 경우만 환불한다(다른 요청이 이미 확정했다면 그대로 둔다)
            return refundIfOrphan(paymentId);
        }
        if (result == PaymentCompleteResult.NOT_PENDING && reservationMapper.countPaidForUser(username, paymentId) > 0) {
            return PaymentCompleteResult.COMPLETED; // 웹훅과 동시에 와서 진 쪽도 결제는 확정된 상태다
        }
        return result;
    }

    @Override
    public PaymentCompleteResult handlePaidWebhook(String paymentId) {
        // 웹훅은 로그인 사용자가 없으므로 paymentId로 예약 주인을 찾는다
        ReservationVO reservation = reservationMapper.findPendingByPaymentId(paymentId);
        if (reservation != null) {
            return completePayment(reservation.getUsername(), paymentId);
        }
        if (reservationMapper.countPaymentRecords(paymentId) > 0) {
            return PaymentCompleteResult.COMPLETED; // 브라우저 요청이 먼저 확정함
        }
        // 만료 작업이 예약을 지운 뒤 결제가 끝났고, 사용자가 창을 닫아 확정 요청도 오지 않은 경우
        return refundIfOrphan(paymentId);
    }

    @Override
    public ExpiryResult expirePendingReservations(int minutes, int batchSize) {
        int confirmed = 0, released = 0, refunded = 0, skipped = 0;
        // 배치 작업이라 예약마다 포트원을 한 번씩 조회한다(건수는 batchSize로 제한)
        for (ReservationVO r : reservationMapper.findExpiredPending(minutes, batchSize)) {
            try {
                PortOnePayment payment = r.getPaymentId() == null ? null : portOneClient.getPayment(r.getPaymentId());
                if (payment != null && payment.isPaid()) {
                    if (sameAmount(payment.totalAmount(), r.getReservation_price())) {
                        // 결제는 끝났는데 확정 요청이 오지 않은 예약(결제 직후 창을 닫음 등) → 지우지 않고 확정
                        if (confirmInTransaction(r.getUsername(), r.getPaymentId(), r) == PaymentCompleteResult.COMPLETED) {
                            confirmed++;
                        } else {
                            skipped++;
                        }
                        continue;
                    }
                    if (refundUnrecorded(r.getPaymentId(), "결제 금액이 예약 금액과 다름") != PaymentCompleteResult.REFUNDED) {
                        skipped++; // 환불이 안 됐으면 예약을 남겨 두고 다음 주기에 다시 본다
                        continue;
                    }
                    refunded++;
                }
                released += reservationMapper.deletePendingByNumber(r.getReservation_number());
            } catch (RuntimeException e) {
                // 포트원 조회 실패 시 지우지 않는다(결제된 좌석을 풀어 버리지 않도록). 다음 주기에 다시 본다
                log.warn("만료 처리 보류 reservation_number={} paymentId={}", r.getReservation_number(), r.getPaymentId(), e);
                skipped++;
            }
        }
        return new ExpiryResult(confirmed, released, refunded, skipped);
    }

    private PaymentCompleteResult confirmInTransaction(String username, String paymentId, ReservationVO reservation) {
        try {
            return transactionTemplate.execute(status -> confirmPaid(username, paymentId, reservation));
        } catch (DuplicateKeyException e) {
            // movie_payment.paymentId 유니크 위반 = 이미 다른 예약 확정에 쓴 결제. 트랜잭션은 롤백된다
            log.warn("이미 사용된 결제로 확정 시도 paymentId={}", paymentId);
            return PaymentCompleteResult.NOT_PENDING;
        }
    }

    /**
     * 어느 예약에도 연결되지 않은 결제만 환불한다.
     * 서버가 만든 paymentId이고 예약·결제 기록 어디에도 없을 때만 → 남의 정상 결제나 예전 결제는 취소되지 않는다.
     */
    private PaymentCompleteResult refundIfOrphan(String paymentId) {
        if (!ReservationService.isServerPaymentId(paymentId) || reservationMapper.countReservationsByPaymentId(paymentId) > 0) {
            return PaymentCompleteResult.RESERVATION_NOT_FOUND;
        }
        PortOnePayment payment = portOneClient.getPayment(paymentId);
        if (payment == null || !payment.isPaid()) {
            return PaymentCompleteResult.RESERVATION_NOT_FOUND;
        }
        return refundUnrecorded(paymentId, "예약 시간이 지나 좌석이 해제됨");
    }

    /** 결제 기록(movie_payment)에 없는 결제만 전액 취소한다. 이미 확정에 쓰인 결제는 건드리지 않는다. */
    private PaymentCompleteResult refundUnrecorded(String paymentId, String reason) {
        if (!ReservationService.isServerPaymentId(paymentId) || reservationMapper.countPaymentRecords(paymentId) > 0) {
            return PaymentCompleteResult.NOT_PENDING;
        }
        try {
            portOneClient.cancelPayment(paymentId, reason);
            log.info("결제 자동 취소 paymentId={} 사유={}", paymentId, reason);
            return PaymentCompleteResult.REFUNDED;
        } catch (RuntimeException e) {
            log.error("결제 자동 취소 실패, 수동 환불 필요 paymentId={} 사유={}", paymentId, reason, e);
            return PaymentCompleteResult.REFUND_FAILED;
        }
    }

    private PaymentCompleteResult confirmPaid(String username, String paymentId, ReservationVO reservation) {
        if (reservationMapper.markPaid(reservation.getReservation_number()) != 1) {
            // 다른 요청이 먼저 확정했거나 만료 작업이 지운 경우
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
