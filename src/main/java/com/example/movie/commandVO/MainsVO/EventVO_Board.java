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
public class EventVO_Board {
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


}
