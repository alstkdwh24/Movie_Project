package com.example.movie.LoginService;


import com.example.movie.commandVO.LoginUpdateVO;
import com.example.movie.commandVO.LoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface LoginMapper {
    int Login_join(LoginVO vo);

  LoginVO login(@Param("username") String username);

    LoginVO LOGIN_VO(@Param("username") String username);

    ArrayList<LoginVO> getAcount(String username);


    void get_Mypage_Modify(@Param("username") String username, @Param("update") LoginUpdateVO update);
}
