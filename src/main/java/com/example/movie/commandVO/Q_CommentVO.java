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
public class Q_CommentVO {
    private Integer comment_number;
    private String comment;
    private LocalDateTime comment_date;
    private String username;
    private String nickname;
    private Integer q_number;
    private String Q_title; // 이 필드가 존재해야 함

}
