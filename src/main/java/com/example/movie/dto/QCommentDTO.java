package com.example.movie.dto;

import com.example.movie.entity.QCommentEntity;
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
public class QCommentDTO {
    private Integer comment_number;
    private String comment;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;

    private String username;
    private String nickname;
    private Integer Q_number;

    public QCommentEntity toEntity() {
        return QCommentEntity.builder()
                .comment_number(this.comment_number)
                .comment(this.comment)
                .comment_date(this.comment_date)
                .username(this.username)
                .nickname(this.nickname)
                .Q_number(this.Q_number)
                .build();
    }

    public static QCommentDTO fromEntity(QCommentEntity entity) {
        if (entity == null) return null;
        return QCommentDTO.builder()
                .comment_number(entity.getComment_number())
                .comment(entity.getComment())
                .comment_date(entity.getComment_date())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .Q_number(entity.getQ_number())
                .build();
    }
}
