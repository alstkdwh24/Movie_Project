package com.example.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatEntity {
    private Integer Q_number;
    private String ID_number;
    private String nickname;
    private String Q_title;
    private String Q_content;
    private Integer Q_count;
    private Integer Q_share_count;
    private String username;
    private LocalDate Q_write_time;
    private Integer comment_number;
    private String comment;
    private LocalDateTime comment_date;

}
