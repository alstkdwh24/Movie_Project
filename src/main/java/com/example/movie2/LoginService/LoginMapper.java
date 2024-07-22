package com.example.movie2.LoginService;


import com.example.movie2.commandVO.LoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {
    int Login_join(LoginVO vo);

    LoginVO login(String id);

    LoginVO Logins(@Param("id") String id);
}
