package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventVO {
    private Integer free_number;
    private Integer free_like;
    private String free_contents;
    private Integer free_count;
    private String free_title;
}
