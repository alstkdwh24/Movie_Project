package com.example.movie.LoginService;


import com.example.movie.commandVO.LoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {
    int Login_join(LoginVO vo);

  public  LoginVO login(@Param("username") String username);

    LoginVO LOGIN_VO(@Param("username") String username);
}
