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
public class Movie_gifticonVO {
    private Integer Gifticon_number;
    private String Gifticon_name;
    private String Gifticon_filename;
    private String uuid;
    private String movie_filepath;
    private LocalDateTime reg_date;
    private String uploadPaths;
    private long size;
    private String movie_resist_filePath;
    private String resist_textarea;
    private String GifticonHtml;
}
