package com.example.movie.LoginService;

import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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



}
