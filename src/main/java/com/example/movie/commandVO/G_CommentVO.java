package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//공지사항 엔터티 VO
public class G_CommentVO {
    private Integer comment_number;
    private String comment;
    private String username;
//    private File userimage;
    private Integer g_number;
    private String nickname;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;
}
