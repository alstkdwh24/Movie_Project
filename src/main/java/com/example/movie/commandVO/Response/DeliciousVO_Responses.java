package com.example.movie.commandVO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliciousVO_Responses {

    private Integer delicious_number;
    private String delicious_url;
    private String delicious_htmlContent;
    private String filePath;
    private String delicious_filename;
    private String delicious_name;
}
