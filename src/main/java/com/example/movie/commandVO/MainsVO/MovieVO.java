package com.example.movie.commandVO.MainsVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieVO {
    private Integer movie_integer;
    private String movie_title;
    private String movie_textarea;
    private String movie_filename;
    private String uuid;
    private LocalDateTime reg_date;
    private String filepath;

    public void setFIleSize(long size) {
    }
}
