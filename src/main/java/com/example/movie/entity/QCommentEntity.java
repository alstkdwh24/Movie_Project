package com.example.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QCommentEntity {
    private Integer comment_number;
    private String comment;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;

    private String username;
    private String nickname;
    private Integer Q_number;
}
