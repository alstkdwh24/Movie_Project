package com.example.movie2.commandVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginVO {
    private Integer ID_number;
    private String nickname;
    private String namena;
    private String id;
    private String pw;
    private Integer phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
private String gender;
private String job;
private String email;
private String role;

}
