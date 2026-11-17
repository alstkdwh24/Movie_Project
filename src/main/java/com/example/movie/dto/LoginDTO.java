package com.example.movie.dto;

import com.example.movie.entity.LoginEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    private Integer ID_number;
    private String nickname;
    private String namena;
    private String pw;
    private Integer phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String gender;
    private String job;
    private String email;
    private String username;
    private String roles;

    public LoginEntity toEntity() {
        return LoginEntity.builder()
                .ID_number(this.ID_number)
                .nickname(this.nickname)
                .namena(this.namena)
                .pw(this.pw)
                .phone(this.phone)
                .birthDate(this.birthDate)
                .gender(this.gender)
                .job(this.job)
                .email(this.email)
                .username(this.username)
                .roles(this.roles)
                .build();
    }

    public static LoginDTO fromEntity(LoginEntity entity) {
        if (entity == null) return null;
        return LoginDTO.builder()
                .ID_number(entity.getID_number())
                .nickname(entity.getNickname())
                .namena(entity.getNamena())
                .pw(entity.getPw())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .gender(entity.getGender())
                .job(entity.getJob())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .roles(entity.getRoles())
                .build();
    }
}
