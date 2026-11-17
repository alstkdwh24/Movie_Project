package com.example.movie.dto.mains;

import com.example.movie.entity.mains.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    private Integer movie_integer;
    private String movie_title;
    private String movie_textarea;
    private String movie_filename;
    private String uuid;
    private LocalDateTime reg_date;
    private String filePath;
    private long size;
    private String movie_resist_filePath;
    private String uploadPaths;

    public MovieEntity toEntity() {
        return MovieEntity.builder()
                .movie_integer(this.movie_integer)
                .movie_title(this.movie_title)
                .movie_textarea(this.movie_textarea)
                .movie_filename(this.movie_filename)
                .uuid(this.uuid)
                .reg_date(this.reg_date)
                .filePath(this.filePath)
                .size(this.size)
                .movie_resist_filePath(this.movie_resist_filePath)
                .uploadPaths(this.uploadPaths)
                .build();
    }

    public static MovieDTO fromEntity(MovieEntity entity) {
        if (entity == null) return null;
        return MovieDTO.builder()
                .movie_integer(entity.getMovie_integer())
                .movie_title(entity.getMovie_title())
                .movie_textarea(entity.getMovie_textarea())
                .movie_filename(entity.getMovie_filename())
                .uuid(entity.getUuid())
                .reg_date(entity.getReg_date())
                .filePath(entity.getFilePath())
                .size(entity.getSize())
                .movie_resist_filePath(entity.getMovie_resist_filePath())
                .uploadPaths(entity.getUploadPaths())
                .build();
    }
}
