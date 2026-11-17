# Movie Backend Project

Spring Boot 2.7.18 + MyBatis + Spring Security + Thymeleaf 기반 영화관 예매·커뮤니티·스토어 백엔드 프로젝트입니다.
포트폴리오용으로 만든 프로젝트이며, 영화 예매부터 포트원(구 아임포트) 카카오페이 결제까지의 흐름을 구현했습니다.

## 주요 기능

| 영역 | 내용 | 주요 코드 |
|---|---|---|
| 회원 | 회원가입·로그인(Spring Security), 권한(`ROLE_USER`) | `LoginController`, `SecuritySet/` |
| 영화 | 영화 등록·목록·상세, 카테고리 | `MovieController`, `MovieApi`, `RestCategoryController` |
| 예매 | 영화·상영관·시간·좌석 선택 후 예매 저장 | `ReservationController`, `ReservationApi`, `ReservationServiceImpl` |
| 결제 | 포트원 카카오페이 결제, 결제 내역 저장·조회 | `PaymentApi`, `static/main.js` |
| 스토어 | 티켓·기프티콘·먹거리(팝콘) 판매 | `StoreController`, `TicketApi`, `GifticonApi`, `DeliciousApi` |
| 이벤트·커뮤니티 | 이벤트 게시판, 자유게시판, 질문·댓글 | `EventController`, `Question_RestController` |
| 채팅 | 채팅 화면·메시지 저장 | `ChatController` |

## 기술 스택

- Java 21, Spring Boot 2.7.18, Gradle
- MyBatis(주 매퍼), Spring Data JPA(의존성 포함), Thymeleaf, Spring Security
- MySQL, 테스트용 H2
- 결제: 포트원(구 아임포트) — 서버 `iamport-rest-client-java`, 브라우저 SDK

## 결제 흐름

1. 브라우저에서 포트원 SDK로 카카오페이 결제를 요청합니다(`static/main.js`).
2. 결제 완료 후 `POST /payment/complete`로 예매·결제 정보를 보냅니다(`PaymentApi` → `ReservationService.payment_loading`).
3. 서버는 `POST /api/Tokens`로 포트원 API(`api.portone.io/login/api-secret`)에 토큰을 요청하고, `GET /payments`로 결제 내역을 조회합니다.
4. 결제 결과는 `movie_payment`, `paymentIdvo`, `movie_payment_paymentId` 테이블에 저장합니다.

## 알려진 한계 / 개선 예정

- `reservation_contents`에 같은 좌석 중복을 막는 유니크 제약이 없어 동시 요청 시 중복 예약이 가능합니다. (영화·상영관·시간·좌석 기준 유니크 제약 추가 예정)
- 결제 금액을 서버에서 포트원 조회 결과와 대조해 검증하는 코드는 확인하지 못했습니다.
- 결제 테이블에 `accessToken` 컬럼이 있어 토큰이 DB에 저장됩니다. 저장하지 않는 방향으로 바꿀 예정입니다.
- `POST /api/Tokens`가 클라이언트가 보낸 `TokenDTO`를 그대로 포트원에 전달하므로, API 시크릿을 브라우저에서 보내는 구조인지 점검하고 서버 설정값으로 옮겨야 합니다.
- `PaymentApi`에 `@CrossOrigin("*")`이 걸려 있어 허용 출처를 좁혀야 합니다.
- `static/main.js`의 결제 호출은 `merchant_uid`가 고정값이고 페이지 로드 시 바로 실행되는 형태라 정리가 필요합니다.
- 실제 운영(실결제) 여부와 PortOne 채널 설정(테스트/실연동)은 이 저장소에서 확인할 수 없습니다.

---

## 1. 패키지 및 아키텍처 구조

기존 `commandVO` 단일 구조를 **계층 간 전달용 `DTO`**와 **DB 매핑용 `Entity`**로 분리했습니다.

- **`com.example.movie.controller` / `RestController`**: 뷰(Thymeleaf) 및 REST API 요청·응답 처리 (`DTO` 사용)
- **`com.example.movie.*Service`**: 비즈니스 로직 처리 및 `DTO ↔ Entity` 변환 (`toEntity()`, `fromEntity()`)
- **`com.example.movie.*Service.*Mapper`**: MyBatis 매퍼 인터페이스 (`Entity` 사용)
- **`com.example.movie.dto`**: 계층 간 전달 객체 (`*DTO`, `mains/*DTO`, `response/*ResponseDTO`)
- **`com.example.movie.entity`**: DB 테이블 1:1 매핑 객체 (`*Entity`, `mains/*Entity`)
- **`com.example.movie.SecuritySet`**: Spring Security 설정(`MovieConfig`), 로컬 업로드 정적 리소스 핸들러(`ResourceHandler`), 사용자 인증(`MyUserDetail`, `MyUserDetailService`)

---

## 2. 로컬 실행 및 빌드/테스트 명령어

### Gradle 테스트 실행 (JDK 21 지정)
```powershell
.\gradlew.bat test --tests com.example.movie.DtoEntityConversionTest '-Dorg.gradle.java.home=C:/Program Files/Java/jdk-21'
```

### 파일 업로드 방식
- 외부 클라우드(AWS S3) 의존성을 제거하고, **로컬 디스크(`project.upload.path`) + Spring `ResourceHandler`** 방식으로 이미지를 저장·서빙합니다.
- 업로드된 파일은 `/css/uploadImage/files/**` 및 `/upload/**` 경로로 즉시 조회됩니다.

---

## 3. MySQL 데이터베이스 및 계정 생성

```sql
-- 1. movie 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS movie
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- 2. 로컬 접속용 DB 계정 생성 ('여기에_아이디', '여기에_비밀번호' 변경)
CREATE USER '여기에_아이디'@'localhost' IDENTIFIED BY '여기에_비밀번호';

-- 3. movie 데이터베이스 권한 부여 및 반영
GRANT ALL PRIVILEGES ON movie.* TO '여기에_아이디'@'localhost';
FLUSH PRIVILEGES;
```

---

## 4. 테이블 생성 DDL (`Entity` 및 MyBatis XML 기준)

```sql
USE movie;

CREATE TABLE IF NOT EXISTS members (
    ID_number INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    pw VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    namena VARCHAR(100),
    phone INT,
    birthDate DATE,
    gender VARCHAR(20),
    job VARCHAR(100),
    email VARCHAR(150),
    roles VARCHAR(50) DEFAULT 'ROLE_USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS question (
    Q_number INT AUTO_INCREMENT PRIMARY KEY,
    ID_number VARCHAR(100),
    username VARCHAR(100),
    nickname VARCHAR(100),
    Q_title VARCHAR(255),
    Q_content TEXT,
    Q_count INT DEFAULT 0,
    Q_share_count INT DEFAULT 0,
    Q_write_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comment_q (
    comment_number INT AUTO_INCREMENT PRIMARY KEY,
    q_number INT NOT NULL,
    username VARCHAR(100),
    nickname VARCHAR(100),
    comment TEXT,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS free_gallery (
    free_number INT AUTO_INCREMENT PRIMARY KEY,
    free_title VARCHAR(255),
    free_contents TEXT,
    username VARCHAR(100),
    nickname VARCHAR(100),
    free_like INT DEFAULT 0,
    free_count INT DEFAULT 0,
    count INT DEFAULT 0,
    write_time VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comment (
    comment_number INT AUTO_INCREMENT PRIMARY KEY,
    free_number INT NOT NULL,
    username VARCHAR(100),
    nickname VARCHAR(100),
    comment TEXT,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS g_gallery (
    g_number INT AUTO_INCREMENT PRIMARY KEY,
    g_title VARCHAR(255),
    g_contents TEXT,
    username VARCHAR(100),
    nickname VARCHAR(100),
    g_like INT DEFAULT 0,
    g_count INT DEFAULT 0,
    g_share_count INT DEFAULT 0,
    count INT DEFAULT 0,
    write_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS comment_g (
    comment_number INT AUTO_INCREMENT PRIMARY KEY,
    g_number INT NOT NULL,
    username VARCHAR(100),
    nickname VARCHAR(100),
    comment TEXT,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_category_table (
    movie_category_number INT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100),
    group_id VARCHAR(50),
    movie_category_lv INT,
    movie_category_nm VARCHAR(255),
    movie_detail_lv INT,
    movie_detail_title VARCHAR(255),
    movie_category_parents_lv INT,
    movie_category_detail_parent_lv INT,
    movie_write_category VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS reservation_contents (
    reservation_number INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    movie_title VARCHAR(255),
    movie_place VARCHAR(255),
    movie_time VARCHAR(100),
    movie_Seat VARCHAR(100),
    reservation_price VARCHAR(50),
    UserPhone VARCHAR(50),
    paymentId VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paymentId VARCHAR(255),
    username VARCHAR(100),
    movie_title VARCHAR(255),
    movie_time VARCHAR(100),
    movie_place VARCHAR(255),
    movie_Seat VARCHAR(100),
    reservation_price VARCHAR(50),
    UserPhone VARCHAR(50),
    accessToken VARCHAR(500),
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_time_two VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS paymentIdvo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paymentId VARCHAR(255),
    reservation_price VARCHAR(50),
    username VARCHAR(100),
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_payment_paymentId (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paymentId VARCHAR(255),
    movie_title VARCHAR(255),
    movie_time VARCHAR(100),
    movie_place VARCHAR(255),
    movie_Seat VARCHAR(100),
    reservation_price VARCHAR(50),
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_time_two VARCHAR(100),
    username VARCHAR(100),
    UserPhone VARCHAR(50),
    accessToken VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_title (
    movie_integer INT AUTO_INCREMENT PRIMARY KEY,
    movie_title VARCHAR(255),
    movie_textarea TEXT,
    movie_filename VARCHAR(255),
    uuid VARCHAR(255),
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    filePath VARCHAR(500),
    sizes BIGINT DEFAULT 0,
    size BIGINT DEFAULT 0,
    movie_resist_filePath VARCHAR(500),
    uploadPaths VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS event_board_image (
    movie_event_image_number INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(255),
    movie_filename VARCHAR(255),
    uuid VARCHAR(255),
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    filePath VARCHAR(500),
    resist_textarea TEXT,
    uploadPaths VARCHAR(500),
    size BIGINT DEFAULT 0,
    movie_resist_filePath VARCHAR(500),
    movie_filepath VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_gifticon (
    Gifticon_number INT AUTO_INCREMENT PRIMARY KEY,
    Gifticon_name VARCHAR(255),
    Gifticon_filename VARCHAR(255),
    uuid VARCHAR(255),
    movie_filepath VARCHAR(500),
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    uploadPaths VARCHAR(500),
    size BIGINT DEFAULT 0,
    movie_resist_filePath VARCHAR(500),
    resist_textarea TEXT,
    GifticonHtml TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS movie_ticket (
    movie_ticket_number INT AUTO_INCREMENT PRIMARY KEY,
    movie_ticket_name VARCHAR(255),
    movie_ticket_filename VARCHAR(255),
    uuid VARCHAR(255),
    reg_date DATE,
    movie_filepath VARCHAR(500),
    uploadPaths VARCHAR(500),
    uploadPath VARCHAR(500),
    filePath VARCHAR(500),
    size BIGINT DEFAULT 0,
    resist_textarea TEXT,
    movie_ticket_html TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS delicious (
    delicious_number INT AUTO_INCREMENT PRIMARY KEY,
    delicious_name VARCHAR(255),
    delicious_filename VARCHAR(255),
    uuid VARCHAR(255),
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    movie_filepath VARCHAR(500),
    resist_textarea TEXT,
    uploadPaths VARCHAR(500),
    size BIGINT DEFAULT 0,
    movie_resist_filePath VARCHAR(500),
    filePath VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS diary_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diary_id BIGINT,
    content TEXT,
    created_at DATE,
    month VARCHAR(20),
    image_url VARCHAR(500),
    user_id BIGINT,
    question_id BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
