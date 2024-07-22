package com.example.movie2.LoginService;

import com.example.movie2.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
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
        String happy=bCryptPasswordEncoder.encode(vo.getPw());
        vo.setPw(happy);
        return loginMapper.Login_join(vo);
    }

    @Override
    public LoginVO login(String id) {
        return loginMapper.login(id);
    }
}
