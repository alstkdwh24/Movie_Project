package com.example.movie.LoginService;


import com.example.movie.commandVO.LoginVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    int Login_join(LoginVO vo);
}
