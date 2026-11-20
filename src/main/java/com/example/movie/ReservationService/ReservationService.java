package com.example.movie.ReservationService;

import com.example.movie.commandVO.*;
import com.example.movie.util.Criteria;

import java.util.ArrayList;
import java.util.List;

public interface ReservationService {
    // 서버가 만드는 paymentId 접두사. 자동 환불은 이 값으로 만든 결제에만 한다
    String PAYMENT_ID_PREFIX = "rsv-";

    /** 서버가 만든 paymentId(rsv- + UUID)인지. 자동 환불은 이 형식에만 한다 */
    static boolean isServerPaymentId(String paymentId) {
        return paymentId != null
                && paymentId.matches("rsv-[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    /** 만료 작업 결과: 결제가 끝나 확정, 결제가 없어 삭제(좌석 해제), 금액이 달라 환불 후 삭제, 확인 실패로 다음 주기에 다시 봄 */
    record ExpiryResult(int confirmed, int released, int refunded, int skipped) {}


    ArrayList<CategoryVO> getMovieCategory();

    ArrayList<CategoryVO> getMovieCategory2();

    ArrayList<CategoryVO> getMovieCategory3();

    ArrayList<CategoryVO> getMovieCategoryChild(CategoryVO vo);

    int getReservation_board(Criteria cri);
    int movie_report_resist(ReservationVO vo);

    List<ReservationVO> getReservation_pay(Criteria cri);

    int reservation_Delete(ReservationVO vo);

    PaymentCompleteResult completePayment(String username, String paymentId);

    ExpiryResult expirePendingReservations(int minutes, int batchSize);

    int paymentVO(PaymentVO vo);

    ArrayList<PaymentVO> getPayment_report(String username);

    ReservationVO paymentId_same(String username,String paymentId);


    int paymentIdSave(PaymentIdVO vo);



    int movie_payment_paymentId(PaymentVO vo);


}
