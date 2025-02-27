package com.example.movie.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginVO {
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
