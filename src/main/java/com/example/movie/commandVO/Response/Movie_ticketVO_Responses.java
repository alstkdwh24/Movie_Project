package com.example.movie.commandVO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie_ticketVO_Responses {
    private String movie_ticket_url;
    private String movie_ticket_htmlContent;
    private String filePath;
    private String movie_ticket_name;
    private String movie_ticket_filename;
    private String movie_filepath;
}
