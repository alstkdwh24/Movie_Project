# 영화관 프로젝트 아키텍처·보안·기능 검토

검토 및 수정 기준일: 2026-10-30  
검토 범위: `src/main/java`, `src/main/resources`, `src/test/java`  
반영 완료 항목:
- 기존 `commandVO` 패키지를 DB 매핑용 `Entity`(`com.example.movie.entity`)와 계층 전달용 `DTO`(`com.example.movie.dto`)로 전면 분리 (MyBatis Mapper 유지)
- 미사용 AWS S3 의존성(`spring-cloud-starter-aws`, `S3Config`, `S3Uploader`) 제거 및 로컬 디스크(`project.upload.path`) + Spring `ResourceHandler` 정적 리소스 서빙 방식으로 전환
- `LoginController`의 평문 비밀번호 로그 출력 및 `RestPaymentController`의 세션 토큰 로그 출력 제거
- 회원가입 역할 강제 고정(`ROLE_USER`), 결제 완료 5단계 검증(`verifyAndCompletePayment`) 및 `PaymentIdList` 소유자·결제번호 조건 검증 반영
- `application.yml` 구성 및 `DtoEntityConversionTest` 단위 테스트(Gradle JDK 21) 검증 완료

---

## 1. 아키텍처 변경 요약 (VO → DTO / Entity 분리)

| 계층 | 패키지 / 위치 | 역할 |
|---|---|---|
| **Controller / RestController** | `com.example.movie.controller`, `com.example.movie.RestController` | 요청 바인딩 및 응답 반환 시 `DTO`(`*DTO`, `*ResponseDTO`) 사용 |
| **Service** | `com.example.movie.*Service` | 비즈니스 로직 처리 및 `dto.toEntity()` / `DTO.fromEntity(entity)` 변환 수행 |
| **Mapper (MyBatis)** | `com.example.movie.*Service.*Mapper`, `resources/templates/movie/mappers/*.xml` | DB 테이블과 1:1 매핑되는 `Entity`(`*Entity`)만 파라미터·결과 타입으로 사용 |
| **Security** | `com.example.movie.SecuritySet` | `MyUserDetailService`에서 `LoginEntity` 조회 후 `LoginDTO` 기반 `MyUserDetail` 생성 (`roles` 권한 연동) |

---

## 2. 최우선 보안 점검 사항

### 2.1 회원가입 권한 고정 및 역할 체계 정리 (`ROLE_USER` / `ROLE_ADMIN`)

- **조치 완료**:
  - `LoginServiceImpl.Login_join()`에서 회원가입 시 `dto.setRoles("ROLE_USER")`로 일반 회원 역할을 서버에서 강제 설정하도록 반영했습니다.
  - `MovieConfig`의 인가 설정을 `.hasAnyRole("USER", "ADMIN")` 및 `.hasRole("ADMIN")`으로 변경하고, Thymeleaf 템플릿(`mains_menus.html`, `sub_menu_fragment.html`)의 역할 비교값을 `ROLE_USER` / `ROLE_ADMIN`으로 통일했습니다.
- **추가 권장**: 관리자 권한(`ROLE_ADMIN`) 부여는 별도 관리자 전용 기능 또는 DB 초기 시드에서만 수행하도록 유지해야 합니다.

### 2.2 결제 결과 검증 및 멱등 처리 (조치 완료 및 추가 권장)

- **조치 완료**:
  - `RestPaymentController`의 `/payment/complete` 요청을 `ReservationServiceImpl.payment_loading()` 및 `@Transactional verifyAndCompletePayment()`로 위임하여 다음 5단계 검증을 모두 적용했습니다.
    1. `existsPaymentById(paymentId)`를 통한 중복 결제(멱등성) 확인 (`409 Conflict`)
    2. `@AuthenticationPrincipal MyUserDetail` 기반 로그인 여부(`401`) 및 요청 `username` 일치 검증(`403`)
    3. `paymentId_same(authenticatedUsername, paymentId)`를 통한 서버 예약 주문 레코드 및 소유자 일치 확인
    4. PortOne 단건 결제 조회 API(`GET https://api.portone.io/payments/{paymentId}`, 타임아웃 5초)를 통한 실제 `PAID` 상태 검증
    5. 서버 예약 금액(`reservation_price`)과 PortOne 실결제 총액(`amount.total`) 일치 검증 후 저장
- **추가 권장**: `/new_payment`(`paymentIdSave`), `/movie_payment_paymentId` 엔드포인트 통합 여부 점검 및 DB 테이블(`movie_payment`, `paymentIdvo`)의 `paymentId` 컬럼 `UNIQUE` 제약 추가 권장

### 2.3 결제 내역 소유자 검사 (조치 완료 및 남은 항목)

- **조치 완료**:
  - `ReservationMapper.xml`의 `PaymentIdList` 쿼리에 `WHERE a.paymentId = #{paymentId} AND a.username = #{username}` 조건을 추가했습니다.
  - `RestPaymentController.paymentId_ListVO`에서 `@AuthenticationPrincipal MyUserDetail`을 통해 비로그인 요청을 차단(`401 Unauthorized`)하고, 로그인한 본인의 `username`만 조회하도록 반영했습니다.
- **남은 항목**:
  - `/movie/payment/report` 엔드포인트도 `@RequestParam("username")` 대신 `@AuthenticationPrincipal MyUserDetail`에서 사용자명을 가져오도록 변경 필요
  - `MovieConfig`의 `/payments/**` 및 결제 관련 API 경로 인가 정책 점검 필요

### 2.4 예약 삭제 시 소유자 검사 부족

예약 번호(`ReservationDTO`)만으로 삭제하지 말고 다음을 함께 확인해야 합니다.

- 예약 번호
- 인증된 사용자 ID
- 예약 상태
- 결제·환불 상태

삭제된 행이 0개이면 성공 응답을 반환하지 않아야 합니다.

### 2.5 파일 업로드 경로 조작 가능성 (로컬 디스크 저장)

현재 이미지 업로드는 AWS S3 대신 로컬 디스크(`project.upload.path`) + `MovieConfig.addResourceHandlers()` 방식으로 동작합니다. 여러 업로드 컨트롤러가 요청의 파일명을 경로에 직접 붙여 저장하고 있으므로 다음 개선이 필요합니다.

개선:

- 파일명은 UUID 등 서버 생성값 사용 (`Paths.get(filename).getFileName()` 등으로 경로 이탈 문자 `..` 차단)
- 정규화한 저장 경로(`normalize()`)가 업로드 루트 내부인지 확인
- 확장자·실제 MIME 타입·용량 검증
- 관리자만 업로드 가능하게 설정

### 2.6 CSRF 전역 비활성화

세션·폼 로그인을 사용하면서 CSRF를 전역 해제하고 있습니다.

- 폼과 AJAX 요청에 CSRF 토큰 적용
- CORS를 CSRF 방어 수단으로 사용하지 않기
- 세션 쿠키의 `HttpOnly`, `Secure`, `SameSite` 확인

### 2.7 기본 접근 정책이 공개 허용

`anyRequest().permitAll()`은 새 API를 추가했을 때 실수로 공개할 위험이 있습니다.

- 기본값을 `authenticated()` 또는 `denyAll()`로 설정
- 공개 API만 명시적으로 허용

### 2.8 비밀번호·토큰 로그 출력 (조치 완료 및 주의사항)

- **조치 완료**: `LoginController`의 평문 비밀번호(`pw`) 출력과 `RestPaymentController`의 세션 토큰 로그 출력을 제거했습니다.
- **주의사항**: 추후 디버깅 시에도 `LoginDTO`, `LoginEntity`, `TokenDTO` 전체 객체나 비밀번호·액세스 토큰을 로그에 남기지 않도록 주의해야 합니다.

---

## 3. 기능적으로 고쳐야 할 부분

### 3.1 중복 예약 방지

자바에서 단순 조회만 하는 것은 동시 요청에 취약합니다.

필수 구성:

1. 서버에서 사용자·가격·상영 정보를 검증
2. 트랜잭션 안에서 예약 처리
3. 영화·상영시간·좌석 조합에 DB 유니크 제약 적용
4. 중복 키 오류를 `409 Conflict`로 변환
5. 좌석 상태를 `AVAILABLE`, `HELD`, `CONFIRMED`, `EXPIRED`, `CANCELLED`로 관리

예시 제약:

```sql
UNIQUE(movie_title, movie_time, movie_Seat)
```

실제 컬럼명 확인 후 적용해야 하며, SQL은 검토 후 사용자가 직접 실행해야 합니다.

### 3.2 예약·결제 트랜잭션

예약 저장, 결제 저장, 결제 ID 저장이 분리되어 있어 중간 실패 시 데이터가 불일치할 수 있습니다.

- 예약 생성과 상태 변경에 `@Transactional` 적용
- 결제 완료 API 멱등 처리
- 결제 성공 후 예약 확정
- 결제 실패·시간 초과 시 좌석 해제

### 3.3 빈 응답 API

`RestReservation`의 `Reservation_Seat()`가 `null`을 반환합니다.

- 실제 좌석 목록을 반환하거나
- 사용하지 않는 엔드포인트라면 제거

### 3.4 페이지네이션 오류 가능성

`Criteria`의 기본 offset 값이 0이고 `setFirst()` 호출이 확인되지 않아 페이지가 바뀌어도 같은 데이터가 반복될 가능성이 있습니다.

또한 예약 조회 SQL은 전체 count와 사용자별 목록이 섞여 있고 `LIMIT`이 없어 페이징이 불완전할 수 있습니다.

### 3.5 외부 API 오류 처리

PortOne 오류를 모두 500으로 반환하지 말고 다음처럼 구분해야 합니다.

- 요청 오류: 400
- 인증 오류: 401
- 권한 오류: 403
- 중복 결제: 409
- 외부 결제 서버 오류: 502

### 3.6 입력값 검증

각 요청 DTO(`ReservationDTO`, `PaymentDTO`, `LoginDTO`, `EventDTO`, `ChatDTO` 등)에 Bean Validation(`@NotBlank`, `@NotNull`, `@Positive` 등)을 적용해야 합니다.

---

## 4. 인증 방식 설명 시 주의

현재 확인된 소스는 JWT보다는 `formLogin + HttpSession` 구조에 가깝습니다. 별도 JWT 필터가 없다면 면접에서는 “Spring Security 세션 기반 인증”이라고 설명하는 것이 코드와 일치합니다.

---

## 5. 추천 수정 순서

1. 결제 서버 검증·중복 결제 방지
2. 결제·예약 API 인증 및 소유자 검사
3. 회원가입 권한 고정 (`LoginServiceImpl`)
4. 파일 업로드 경로(`project.upload.path`) 및 파일명 검증
5. CSRF·CORS 재설정
6. 좌석 중복 예약 방지와 트랜잭션 적용
7. 페이지네이션·오류 응답·DTO 입력 검증 개선
