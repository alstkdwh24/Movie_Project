package com.example.movie2.commandVO;

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


    private Integer g_number;
    private Integer g_like;
    private String g_contents;
    private Integer g_count;
    private String g_title;
    private Integer g_share_count;

}
