package com.example.movie.LoginService;

import com.example.movie.commandVO.LoginUpdateVO;
import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public int Login_join(LoginVO vo) {
        String password= bCryptPasswordEncoder.encode(vo.getPw());
        vo.setPw(password);


        return loginMapper.Login_join(vo);

    }

    @Override
    public LoginVO login(String username){

        return loginMapper.login(username);
    }

    @Override
    public ArrayList<LoginVO> getAcount(String username) {
        return loginMapper.getAcount( username);
    }

//    회원정보 수정
@Override
public void get_Mypage_Modify(String username, LoginUpdateVO update) {
    // 사용자 정보 업데이트
    loginMapper.get_Mypage_Modify(username, update);
}


}
