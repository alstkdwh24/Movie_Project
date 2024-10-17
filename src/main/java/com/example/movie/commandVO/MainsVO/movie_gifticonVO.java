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
public class movie_gifticonVO {
    private Integer Gifticon_number;
    private String Gifticon_name;
    private String Gifticon_filename;
    private String uuid;
    private String movie_filepath;
    private LocalDateTime reg_date;
}
