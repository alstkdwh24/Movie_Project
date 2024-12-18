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
public class DeliciousVO {
    private Integer delicious_number;
    private String delicious_name;
    private String delicious_filename;
    private String uuid;
    private LocalDateTime reg_date;
    private String movie_filepath;
    private String resist_textarea;
    private String uploadPaths;
    private long size;
    private String movie_resist_filePath;
    private String filePath;
}
