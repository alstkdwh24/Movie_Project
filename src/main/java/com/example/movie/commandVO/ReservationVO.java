package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationVO {
    private String movieTitle;  // CamelCase로 변경
    private String movieTime;    // CamelCase로 변경
    private String moviePlace;   // CamelCase로 변경
    private Integer reservation_number;
}