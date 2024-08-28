package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventVO {
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
    private LocalDateTime write_time;

    private Integer g_number;
    private Integer g_like;
    private String g_contents;
    private Integer g_count;
    private String g_title;
    private Integer g_share_count;

}
