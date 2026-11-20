-- 예약 상태(PENDING/PAID)와 예약 시각 컬럼 추가
-- 배포 전에 운영 MySQL에서 직접 실행한다. 행을 지우거나 바꾸지 않고 컬럼만 더한다.

-- 1) 실행 전 확인: 지금 남아 있는 예약 행 수
--    예전 결제 흐름은 결제가 끝나면 예약 행을 지웠으므로, 남아 있는 행은 결제하지 않은 예약이다.
SELECT COUNT(*) AS pending_rows FROM reservation_contents;

-- 2) 컬럼 추가. 기존 행은 status='PENDING', reserved_at=실행 시각이 된다.
--    배포 후 만료 작업(기본 10분)이 결제하지 않은 기존 예약을 지워 좌석을 푼다.
ALTER TABLE reservation_contents
    ADD COLUMN status      VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    ADD COLUMN reserved_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 3) 결제 1건을 여러 예약 확정에 다시 쓰지 못하도록 paymentId를 유니크로 건다.
--    먼저 중복이 있는지 확인한다. 아래 두 SELECT가 0행이어야 인덱스를 만들 수 있다.
SELECT paymentId, COUNT(*) FROM movie_payment GROUP BY paymentId HAVING COUNT(*) > 1 LIMIT 20;
SELECT paymentId, COUNT(*) FROM reservation_contents GROUP BY paymentId HAVING COUNT(*) > 1 LIMIT 20;

CREATE UNIQUE INDEX uq_movie_payment_paymentId ON movie_payment (paymentId);
CREATE UNIQUE INDEX uq_reservation_paymentId ON reservation_contents (paymentId);

-- 4) 만료 작업이 status·reserved_at으로 찾으므로 인덱스를 둔다.
CREATE INDEX idx_reservation_status_time ON reservation_contents (status, reserved_at);
