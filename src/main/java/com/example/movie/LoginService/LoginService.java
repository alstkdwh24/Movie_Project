package com.example.movie.LoginService;

import com.example.movie.dto.LoginDTO;
import com.example.movie.dto.LoginUpdateDTO;

import java.util.ArrayList;

public interface LoginService {
    int Login_join(LoginDTO dto);

    LoginDTO login(String username);

    ArrayList<LoginDTO> getAcount(String username);

    // 회원정보 수정
    void get_Mypage_Modify(String username, LoginUpdateDTO update);
}
