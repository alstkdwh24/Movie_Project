# 영화관 예매·결제 사이트 (JCGX)

영화 예매, 포트원 결제, 커뮤니티 게시판을 갖춘 영화관 웹사이트입니다.
2024.04 ~ 2024.12, 개인 프로젝트로 설계부터 배포까지 진행했습니다.

## 기술 스택

- Java 21, Spring Boot 2.7, Spring Security (BCrypt 비밀번호 암호화)
- MyBatis (Mapper XML), MySQL
- Thymeleaf, jQuery
- 포트원(아임포트) 결제 연동, AWS S3 파일 업로드

## 기능

- **예매**: 영화 · 지점 · 시간 · 좌석 선택, 예매 취소
  - 단계를 넘어갈 때 선택값이 사라지지 않도록 `data` 속성과 SessionStorage로 유지
- **결제**: 포트원 서버 연동(서버에서 액세스 토큰 발급), 결제 내역 저장·조회, 예약 리포트
- **회원**: 회원가입 · 로그인(Spring Security), 마이페이지
- **게시판**: 자유 · 갤러리 · 맛집 · 이벤트 · Q&A 5종 등록·수정·삭제, 댓글 · 조회수, Q&A 페이징
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

## 회고

당시 영화 제목과 별명을 PK로 잡아, 값이 바뀌면 참조하는 테이블을 모두 고쳐야 하는 구조였습니다.
이후 JO-GPT와 LeavePlanner에서는 의미 없는 대리키(member_key, UUID)를 PK로 두었습니다.
