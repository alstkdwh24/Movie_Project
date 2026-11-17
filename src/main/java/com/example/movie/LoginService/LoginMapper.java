package com.example.movie.LoginService;

import com.example.movie.entity.LoginEntity;
import com.example.movie.entity.LoginUpdateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface LoginMapper {
    int Login_join(LoginEntity entity);

    LoginEntity login(@Param("username") String username);

    LoginEntity LOGIN_VO(@Param("username") String username);

    ArrayList<LoginEntity> getAcount(String username);

    void get_Mypage_Modify(@Param("username") String username, @Param("update") LoginUpdateEntity update);
}
