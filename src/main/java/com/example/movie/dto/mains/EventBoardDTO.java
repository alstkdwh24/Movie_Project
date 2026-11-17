package com.example.movie.dto.mains;

import com.example.movie.entity.mains.EventBoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventBoardDTO {
    private Integer movie_event_image_number;
    private String event_name;
    private String movie_filename;
    private String uuid;
    private LocalDateTime reg_date;
    private String filePath;
    private String resist_textarea;
    private String uploadPaths;
    private long size;
    private String movie_resist_filePath;
    private String movie_filepath;

    public EventBoardEntity toEntity() {
        return EventBoardEntity.builder()
                .movie_event_image_number(this.movie_event_image_number)
                .event_name(this.event_name)
                .movie_filename(this.movie_filename)
                .uuid(this.uuid)
                .reg_date(this.reg_date)
                .filePath(this.filePath)
                .resist_textarea(this.resist_textarea)
                .uploadPaths(this.uploadPaths)
                .size(this.size)
                .movie_resist_filePath(this.movie_resist_filePath)
                .movie_filepath(this.movie_filepath)
                .build();
    }

    public static EventBoardDTO fromEntity(EventBoardEntity entity) {
        if (entity == null) return null;
        return EventBoardDTO.builder()
                .movie_event_image_number(entity.getMovie_event_image_number())
                .event_name(entity.getEvent_name())
                .movie_filename(entity.getMovie_filename())
                .uuid(entity.getUuid())
                .reg_date(entity.getReg_date())
                .filePath(entity.getFilePath())
                .resist_textarea(entity.getResist_textarea())
                .uploadPaths(entity.getUploadPaths())
                .size(entity.getSize())
                .movie_resist_filePath(entity.getMovie_resist_filePath())
                .movie_filepath(entity.getMovie_filepath())
                .build();
    }
}
