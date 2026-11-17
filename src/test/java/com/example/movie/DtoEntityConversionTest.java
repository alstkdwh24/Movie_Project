package com.example.movie;

import com.example.movie.ChatService.ChatMapper;
import com.example.movie.ChatService.ChatServiceImpl;
import com.example.movie.LoginService.LoginMapper;
import com.example.movie.LoginService.LoginServiceImpl;
import com.example.movie.ReservationService.ReservationMapper;
import com.example.movie.ReservationService.ReservationServiceImpl;
import com.example.movie.SecuritySet.MyUserDetail;
import com.example.movie.SecuritySet.MyUserDetailService;
import com.example.movie.ShopService.ShopMapper;
import com.example.movie.ShopService.ShopServiceImpl;
import com.example.movie.communityEventService.EventMapper;
import com.example.movie.communityEventService.EventServiceImpl;
import com.example.movie.dto.*;
import com.example.movie.dto.mains.*;
import com.example.movie.entity.*;
import com.example.movie.entity.mains.*;
import com.example.movie.movie_image_service.ImageMapper;
import com.example.movie.movie_image_service.ImageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DtoEntityConversionTest {

    @Mock
    private LoginMapper loginMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl loginService;

    @InjectMocks
    private MyUserDetailService myUserDetailService;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ImageMapper movieImageMapper;

    @InjectMocks
    private ImageServiceImpl movieImageService;

    @Mock
    private ShopMapper shopMapper;

    @InjectMocks
    private ShopServiceImpl shopService;

    @Test
    @DisplayName("전체 DTO <-> Entity 양방향 변환 검증")
    void testAllDtoEntityConversions() {
        // 1. Category
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .movie_category_lv(1)
                .movie_category_nm("액션")
                .group_id("A")
                .build();
        CategoryEntity categoryEntity = categoryDTO.toEntity();
        assertThat(CategoryDTO.fromEntity(categoryEntity)).isEqualTo(categoryDTO);

        // 2. Chat
        ChatDTO chatDTO = ChatDTO.builder()
                .Q_number(10)
                .Q_title("문의 제목")
                .Q_content("문의 내용")
                .username("user1")
                .nickname("닉네임")
                .Q_write_time(LocalDate.of(2026, 10, 30))
                .build();
        ChatEntity chatEntity = chatDTO.toEntity();
        assertThat(ChatDTO.fromEntity(chatEntity)).isEqualTo(chatDTO);

        // 3. Diary
        DiaryDTO diaryDTO = DiaryDTO.builder()
                .id(1L)
                .diaryId(100L)
                .content("다이어리")
                .build();
        assertThat(DiaryDTO.fromEntity(diaryDTO.toEntity())).isEqualTo(diaryDTO);

        // 4. Event
        EventDTO eventDTO = EventDTO.builder()
                .id(1)
                .free_number(5)
                .free_title("자유게시판 제목")
                .free_contents("내용")
                .g_number(2)
                .g_title("공지 제목")
                .username("user1")
                .build();
        EventEntity eventEntity = eventDTO.toEntity();
        EventDTO convertedEvent = EventDTO.fromEntity(eventEntity);
        assertThat(convertedEvent.getFree_number()).isEqualTo(5);
        assertThat(convertedEvent.getFree_title()).isEqualTo("자유게시판 제목");
        assertThat(convertedEvent.getG_title()).isEqualTo("공지 제목");

        // 5. G_Comment
        GCommentDTO gCommentDTO = GCommentDTO.builder()
                .comment_number(1)
                .comment("공지 댓글")
                .g_number(2)
                .nickname("관리자")
                .build();
        assertThat(GCommentDTO.fromEntity(gCommentDTO.toEntity())).isEqualTo(gCommentDTO);

        // 6. Login & LoginUpdate
        LoginDTO loginDTO = LoginDTO.builder()
                .ID_number(1)
                .username("tester")
                .pw("encoded_pw")
                .nickname("테스터")
                .roles("ROLE_USER")
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
        assertThat(LoginDTO.fromEntity(loginDTO.toEntity())).isEqualTo(loginDTO);

        LoginUpdateDTO loginUpdateDTO = LoginUpdateDTO.builder()
                .username("tester")
                .nickname("수정닉네임")
                .roles("ROLE_USER")
                .build();
        assertThat(LoginUpdateDTO.fromEntity(loginUpdateDTO.toEntity())).isEqualTo(loginUpdateDTO);

        // 7. Payment, PaymentId, PaymentId_List
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentId("pay_123")
                .movie_title("범죄도시")
                .reservation_price("15000")
                .username("tester")
                .build();
        assertThat(PaymentDTO.fromEntity(paymentDTO.toEntity())).isEqualTo(paymentDTO);

        PaymentIdDTO paymentIdDTO = PaymentIdDTO.builder()
                .paymentId("pay_123")
                .reservation_price("15000")
                .username("tester")
                .payment_time(LocalDateTime.now())
                .build();
        assertThat(PaymentIdDTO.fromEntity(paymentIdDTO.toEntity())).isEqualTo(paymentIdDTO);

        PaymentListDTO paymentIdListDTO = PaymentListDTO.builder()
                .paymentId("pay_123")
                .movie_title("범죄도시")
                .username("tester")
                .build();
        assertThat(PaymentListDTO.fromEntity(paymentIdListDTO.toEntity())).isEqualTo(paymentIdListDTO);

        // 8. Q_Comment & Reservation
        QCommentDTO qCommentDTO = QCommentDTO.builder()
                .comment_number(3)
                .comment("답변입니다")
                .Q_number(10)
                .build();
        assertThat(QCommentDTO.fromEntity(qCommentDTO.toEntity())).isEqualTo(qCommentDTO);

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .reservation_number(7)
                .movieTitle("인터스텔라")
                .movieSeat("A1")
                .username("tester")
                .paymentId("pay_123")
                .build();
        assertThat(ReservationDTO.fromEntity(reservationDTO.toEntity())).isEqualTo(reservationDTO);

        // 9. Mains DTOs (Delicious, EventBoard, Movie, Movie_gifticon, Movie_ticket)
        DeliciousDTO deliciousDTO = DeliciousDTO.builder()
                .delicious_number(1)
                .delicious_name("팝콘 세트")
                .size(1024L)
                .build();
        assertThat(DeliciousDTO.fromEntity(deliciousDTO.toEntity())).isEqualTo(deliciousDTO);

        EventBoardDTO eventBoardDTO = EventBoardDTO.builder()
                .movie_event_image_number(1)
                .event_name("할인 이벤트")
                .build();
        assertThat(EventBoardDTO.fromEntity(eventBoardDTO.toEntity())).isEqualTo(eventBoardDTO);

        MovieDTO movieDTO = MovieDTO.builder()
                .movie_integer(1)
                .movie_title("인셉션")
                .filePath("20261030")
                .movie_filename("poster.jpg")
                .build();
        assertThat(MovieDTO.fromEntity(movieDTO.toEntity())).isEqualTo(movieDTO);

        GifticonDTO gifticonDTO = GifticonDTO.builder()
                .Gifticon_number(1)
                .Gifticon_name("기프티콘")
                .build();
        assertThat(GifticonDTO.fromEntity(gifticonDTO.toEntity())).isEqualTo(gifticonDTO);

        TicketDTO ticketDTO = TicketDTO.builder()
                .movie_ticket_number(1)
                .movie_ticket_name("관람권")
                .build();
        assertThat(TicketDTO.fromEntity(ticketDTO.toEntity())).isEqualTo(ticketDTO);
    }

    @Test
    @DisplayName("Service 및 Security 계층 DTO <-> Entity 연동 검증")
    void testServiceAndSecurityLayerWithMocks() {
        // 1. 회원가입 및 로그인/Security 검증
        when(passwordEncoder.encode("raw_pw")).thenReturn("encoded_pw");
        when(loginMapper.Login_join(any(LoginEntity.class))).thenReturn(1);

        LoginDTO joinDto = LoginDTO.builder()
                .username("tester")
                .pw("raw_pw")
                .roles("ROLE_ADMIN")
                .build();
        int joinResult = loginService.Login_join(joinDto);
        assertThat(joinResult).isEqualTo(1);
        assertThat(joinDto.getPw()).isEqualTo("encoded_pw");
        assertThat(joinDto.getRoles()).isEqualTo("ROLE_USER");
        verify(loginMapper).Login_join(any(LoginEntity.class));

        LoginEntity savedEntity = LoginEntity.builder()
                .username("tester")
                .pw("encoded_pw")
                .roles("ROLE_USER")
                .build();
        when(loginMapper.login("tester")).thenReturn(savedEntity);

        UserDetails userDetails = myUserDetailService.loadUserByUsername("tester");
        assertThat(userDetails.getUsername()).isEqualTo("tester");
        assertThat(userDetails.getPassword()).isEqualTo("encoded_pw");
        assertThat(((MyUserDetail) userDetails).getrole()).isEqualTo("ROLE_USER");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");

        // 2. 예매/결제 서비스 검증
        ArrayList<CategoryEntity> categoryEntities = new ArrayList<>(List.of(
                CategoryEntity.builder().movie_category_lv(1).movie_category_nm("서울").build()
        ));
        when(reservationMapper.getMovieCategory()).thenReturn(categoryEntities);

        ArrayList<CategoryDTO> categoryDtos = reservationService.getMovieCategory();
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0).getMovie_category_nm()).isEqualTo("서울");
    }
}
