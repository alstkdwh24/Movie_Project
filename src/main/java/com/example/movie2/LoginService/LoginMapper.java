package com.example.movie2.LoginService;


import com.example.movie2.commandVO.LoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    int Login_join(LoginVO vo);
}
