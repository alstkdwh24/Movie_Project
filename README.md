# 영화관 예매·결제 사이트 (JCGX)

영화 예매, 포트원 결제, 커뮤니티 게시판을 갖춘 영화관 웹사이트입니다.
2024.04 ~ 2024.12, 개인 프로젝트로 설계부터 배포까지 진행했습니다.

## 기술 스택

- Java 21, Spring Boot 2.7, Spring Security (BCrypt 비밀번호 암호화)
- MyBatis (Mapper XML), MySQL
- Thymeleaf, jQuery
- 포트원(아임포트) 결제 연동, AWS S3 파일 업로드

## 기능

- **예매**: 영화 · 지점 · 시간 · 좌석 선택, 결제 전 예매 취소(본인 예약만)
  - 좌석 가격과 예약자는 서버가 정한다(로그인 사용자, `reservation.seat-price`)
  - 결제하지 않은 예약은 `reservation.pending-minutes`(기본 10분)가 지나면 지워져 좌석이 풀린다. 지우기 전에 포트원을 확인해, 이미 결제된 예약(결제 후 창을 닫은 경우 등)은 확정한다
  - 단계를 넘어갈 때 선택값이 사라지지 않도록 `data` 속성과 SessionStorage로 유지
- **결제**: 포트원 V2. 결제 후 서버가 포트원에서 결제를 직접 조회해 상태(PAID)와 금액을 DB 예약 금액과 비교한 뒤에만 저장한다
  - 예약이 만료로 지워진 뒤 결제가 끝났거나 금액이 다르면, 결제 확정 요청이 서버에 도착했을 때 포트원 결제 취소 API로 자동 환불한다(서버가 만든 `rsv-` 결제 중 확정에 쓰이지 않은 것만)
  - 결제 후 창을 닫아 확정 요청이 오지 않아도 포트원 웹훅(`Transaction.Paid` → `POST /payment/webhook`)으로 확정하거나 환불한다. 서명(Standard Webhooks)이 맞는 요청만 받고, 본문을 믿지 않고 포트원에서 결제를 다시 조회한다
- **회원**: 회원가입 · 로그인(Spring Security), 마이페이지
- **게시판**: 자유 · 갤러리 · Q&A 3종 작성·조회, 댓글 · 조회수, 페이징
- **스토어**: 팝콘 · 기프티콘 · 관람권 상품 등록과 목록
- **DB**: ERD 설계 후 조건별 JOIN · INSERT · SELECT · UPDATE · DELETE 작성

## 실행 방법

### 준비물

- JDK 21
- MySQL 8

### 1. 설정 파일 만들기

`src/main/resources/application.yml`은 저장소에 포함하지 않았습니다. 아래 형식으로 만들고 값을 채웁니다.
비밀값은 파일에 직접 쓰지 않고 환경변수로 넘깁니다.

```yaml
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${DB_URL}            # 예: jdbc:mysql://localhost:3306/movie
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: none
  servlet:
    multipart:
      max-file-size: 10MB      # 예시 값
      max-request-size: 10MB

mybatis:
  mapper-locations: classpath:/templates/movie/mappers/*.xml

project:
  upload:
    path: ${UPLOAD_PATH}      # 업로드 파일을 저장할 로컬 폴더

portone:
  api-secret: ${PORTONE_API_SECRET}   # 서버에서만 쓴다. JS·저장소에 넣지 않는다
  store-id: ${PORTONE_STORE_ID}       # 결제창에 넘기는 공개 식별자
  channel-key: ${PORTONE_CHANNEL_KEY}
  webhook-secret: ${PORTONE_WEBHOOK_SECRET}  # 콘솔 결제알림(Webhook) 관리에서 발급(whsec_...)

reservation:
  seat-price: 1000          # 좌석 1개 가격(원)
  pending-minutes: 10       # 결제하지 않은 예약을 지우는 기준

cloud:
  aws:
    credentials:
      access-Key: ${AWS_ACCESS_KEY}
      secret-Key: ${AWS_SECRET_KEY}
    region:
      static: ${AWS_REGION}    # 예: ap-northeast-2
    s3:
      bucket: ${AWS_S3_BUCKET}
    stack:
      auto: false
```

### 2. 실행

```bash
./gradlew bootRun
```

Windows에서는 `gradlew.bat bootRun`을 실행합니다. 실행 후 `http://localhost:8080/mains`로 접속합니다.

> 테이블 생성 SQL은 저장소에 포함되어 있지 않습니다. `ddl-auto: none`이므로 MySQL에 테이블을 먼저 만들어야 합니다.
> 예약 상태 컬럼과 paymentId 유니크 인덱스는 `docs/sql/20261121_reservation_status.sql`로 추가합니다.
> 배포 순서: ① SQL 실행(이전 버전 코드와도 호환) → ② `portone.*` 설정 → ③ 새 버전 배포 → ④ 포트원 콘솔에 웹훅 주소 `https://<도메인>/payment/webhook` 등록(버전 2024-04-25). SQL 없이 배포하면 예약 조회·취소·결제 확정이 실패합니다.

## 회고

당시 영화 제목과 별명을 PK로 잡아, 값이 바뀌면 참조하는 테이블을 모두 고쳐야 하는 구조였습니다.
이후 JO-GPT와 LeavePlanner에서는 의미 없는 대리키(member_key, UUID)를 PK로 두었습니다.
