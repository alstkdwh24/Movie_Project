package com.example.movie.dto;

import com.example.movie.entity.DiaryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryDTO {
    private Long diaryId;
    private String content;
    private LocalDate createdAt;
    private String month;
    private String imageUrl;
    private Long userId;
    private Long questionId;
    private Long id;

    public DiaryEntity toEntity() {
        return DiaryEntity.builder()
                .diaryId(this.diaryId)
                .content(this.content)
                .createdAt(this.createdAt)
                .month(this.month)
                .imageUrl(this.imageUrl)
                .userId(this.userId)
                .questionId(this.questionId)
                .id(this.id)
                .build();
    }

    public static DiaryDTO fromEntity(DiaryEntity entity) {
        if (entity == null) return null;
        return DiaryDTO.builder()
                .diaryId(entity.getDiaryId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .month(entity.getMonth())
                .imageUrl(entity.getImageUrl())
                .userId(entity.getUserId())
                .questionId(entity.getQuestionId())
                .id(entity.getId())
                .build();
    }
}
