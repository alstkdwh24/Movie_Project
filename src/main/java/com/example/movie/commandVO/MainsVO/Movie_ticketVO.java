package com.example.movie.commandVO.MainsVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie_ticketVO {
    private Integer movie_ticket_number;
    private String movie_ticket_name;
    private String movie_ticket_filename;
    private String uuid;
    private LocalDate reg_date;
    private String movie_filepath;
    private String uploadPaths;
    private String uploadPath;
    private String filePath;
    private long size;
    private String resist_textarea;
    private String movie_ticket_html;
}
