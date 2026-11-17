package com.example.movie.dto;

import com.example.movie.entity.EventEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {
    private Integer id;
    private Integer count;
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

    private String comment;
    @DateTimeFormat(pattern = "MM-dd-HH-mm")
    private LocalDate comment_date;
    private Integer comment_number;

    public int getFreeNumber() {
        return free_number;
    }

    public EventEntity toEntity() {
        return EventEntity.builder()
                .id(this.id)
                .count(this.count)
                .username(this.username)
                .nickname(this.nickname)
                .free_number(this.free_number)
                .free_like(this.free_like)
                .free_contents(this.free_contents)
                .free_count(this.free_count)
                .free_title(this.free_title)
                .write_time(this.write_time)
                .g_number(this.g_number)
                .g_like(this.g_like)
                .g_contents(this.g_contents)
                .g_count(this.g_count)
                .g_title(this.g_title)
                .g_share_count(this.g_share_count)
                .comment(this.comment)
                .comment_date(this.comment_date)
                .comment_number(this.comment_number)
                .build();
    }

    public static EventDTO fromEntity(EventEntity entity) {
        if (entity == null) return null;
        return EventDTO.builder()
                .id(entity.getId())
                .count(entity.getCount())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .free_number(entity.getFree_number())
                .free_like(entity.getFree_like())
                .free_contents(entity.getFree_contents())
                .free_count(entity.getFree_count())
                .free_title(entity.getFree_title())
                .write_time(entity.getWrite_time())
                .g_number(entity.getG_number())
                .g_like(entity.getG_like())
                .g_contents(entity.getG_contents())
                .g_count(entity.getG_count())
                .g_title(entity.getG_title())
                .g_share_count(entity.getG_share_count())
                .comment(entity.getComment())
                .comment_date(entity.getComment_date())
                .comment_number(entity.getComment_number())
                .build();
    }
}
