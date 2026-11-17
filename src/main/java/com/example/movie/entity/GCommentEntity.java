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
public class GCommentEntity {
    private Integer comment_number;
    private String comment;
    private String username;
    private Integer g_number;
    private String nickname;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;
}
