package com.example.movie.dto;

import com.example.movie.entity.ChatEntity;
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
public class ChatDTO {
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

    public ChatEntity toEntity() {
        return ChatEntity.builder()
                .Q_number(this.Q_number)
                .ID_number(this.ID_number)
                .nickname(this.nickname)
                .Q_title(this.Q_title)
                .Q_content(this.Q_content)
                .Q_count(this.Q_count)
                .Q_share_count(this.Q_share_count)
                .username(this.username)
                .Q_write_time(this.Q_write_time)
                .comment_number(this.comment_number)
                .comment(this.comment)
                .comment_date(this.comment_date)
                .build();
    }

    public static ChatDTO fromEntity(ChatEntity entity) {
        if (entity == null) return null;
        return ChatDTO.builder()
                .Q_number(entity.getQ_number())
                .ID_number(entity.getID_number())
                .nickname(entity.getNickname())
                .Q_title(entity.getQ_title())
                .Q_content(entity.getQ_content())
                .Q_count(entity.getQ_count())
                .Q_share_count(entity.getQ_share_count())
                .username(entity.getUsername())
                .Q_write_time(entity.getQ_write_time())
                .comment_number(entity.getComment_number())
                .comment(entity.getComment())
                .comment_date(entity.getComment_date())
                .build();
    }
}
