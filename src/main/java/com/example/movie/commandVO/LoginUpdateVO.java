package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUpdateVO {
    private Integer ID_number;
    private String nickname;
    private String namena;
    private String pw;
    private Integer phone;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;


    private String gender;
    private String job;
    private String email;
    private String username;
    private String roles;


}
