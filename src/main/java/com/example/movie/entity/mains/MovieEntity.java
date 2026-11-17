package com.example.movie.entity.mains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieEntity {
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
}
