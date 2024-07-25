package com.example.movie.LoginService;

import com.example.movie.commandVO.LoginVO;
import org.apache.ibatis.annotations.Param;

public interface LoginService {
     int Login_join(LoginVO vo);

     public LoginVO login(String username);





}
