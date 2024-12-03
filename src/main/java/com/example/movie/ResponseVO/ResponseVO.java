package com.example.movie.ResponseVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseVO {

    private String imageUrl;
    private String htmlContent;
}
