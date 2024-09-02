package com.example.movie.commandVO;

import com.example.movie.util.TimeEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EventVO extends TimeEntity {
    //시간 엔터티에서 상속
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


//    조회수 증가 부분 변수
    private Integer count;
//    일반변수
    private String username;
    private String nickname;
    private Integer free_number;
    private Integer free_like;
    private String free_contents;
    private Integer free_count;
    private String free_title;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate write_time;


    private Integer g_number;
    private Integer g_like;
    private String g_contents;
    private Integer g_count;
    private String g_title;
    private Integer g_share_count;



//    댓글 관련한 부분
    private String comment;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate coment_date;
    private Integer coment_number;
//    private File user_image;

}
