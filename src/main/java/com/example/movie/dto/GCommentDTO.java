package com.example.movie.dto;

import com.example.movie.entity.GCommentEntity;
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
public class GCommentDTO {
    private Integer comment_number;
    private String comment;
    private String username;
    private Integer g_number;
    private String nickname;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;

    public GCommentEntity toEntity() {
        return GCommentEntity.builder()
                .comment_number(this.comment_number)
                .comment(this.comment)
                .username(this.username)
                .g_number(this.g_number)
                .nickname(this.nickname)
                .comment_date(this.comment_date)
                .build();
    }

    public static GCommentDTO fromEntity(GCommentEntity entity) {
        if (entity == null) return null;
        return GCommentDTO.builder()
                .comment_number(entity.getComment_number())
                .comment(entity.getComment())
                .username(entity.getUsername())
                .g_number(entity.getG_number())
                .nickname(entity.getNickname())
                .comment_date(entity.getComment_date())
                .build();
    }
}
