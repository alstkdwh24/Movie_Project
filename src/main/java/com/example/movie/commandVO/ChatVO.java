package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatVO {
    private Integer Q_number;
    private String ID_number;
    private String nickname;
    private String Q_title;
    private String Q_content;
    private Integer Q_count;
    private Integer Q_share_count;
    private String username;
private LocalDate Q_write_time;
}
