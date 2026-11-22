-- 좌석 중복 예매 방지: 같은 영화·지점·시간·좌석이 두 번 저장되지 않도록 막는 유니크 제약
-- 운영 MySQL에는 이미 적용돼 있다(SHOW INDEX FROM reservation_contents로 확인).
-- 이미 적용된 DB에서 실행하면 "1061 Duplicate key name 'uq_reservation_seat'" 오류가 나는데, 이게 정상이며 아무것도 바뀌지 않는다.
-- 새로 만드는 DB에서만 실행한다. 중복 요청은 애플리케이션에서 DuplicateKeyException → 409로 응답한다.

-- 1) 실행 전 확인: 이미 겹친 좌석이 있으면 제약을 만들 수 없다. 0행이어야 한다.
SELECT movie_title, movie_place, movie_time, movie_Seat, COUNT(*) AS cnt
FROM reservation_contents
GROUP BY movie_title, movie_place, movie_time, movie_Seat
HAVING COUNT(*) > 1
LIMIT 20;

-- 2) 제약 추가
ALTER TABLE reservation_contents
    ADD CONSTRAINT uq_reservation_seat
    UNIQUE (movie_title, movie_place, movie_time, movie_Seat);
