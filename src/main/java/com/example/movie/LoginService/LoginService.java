package com.example.movie.LoginService;

import com.example.movie.commandVO.LoginUpdateVO;
import com.example.movie.commandVO.LoginVO;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface LoginService {
     int Login_join(LoginVO vo);

     LoginVO login(String username);


     ArrayList<LoginVO> getAcount(String username);

     //회원정보 수정
     void get_Mypage_Modify(String username, LoginUpdateVO update);


}
