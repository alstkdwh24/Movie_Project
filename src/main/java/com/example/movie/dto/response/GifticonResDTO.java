package com.example.movie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GifticonResDTO {
    private String imageUrl;
    private String htmlContent;
    private String filePath;
    private String gifticon_filename;
    private String gifticon_name;
    private String imageUrl_two;
}
