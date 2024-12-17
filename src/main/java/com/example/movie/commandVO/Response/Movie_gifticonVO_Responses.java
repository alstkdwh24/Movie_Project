package com.example.movie.commandVO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie_gifticonVO_Responses {
    private String imageUrl;
    private String htmlContent;
    private String filePath;
    private String gifticon_filename;

    private String gifticon_name;
    private String imageUrl_two;
}
