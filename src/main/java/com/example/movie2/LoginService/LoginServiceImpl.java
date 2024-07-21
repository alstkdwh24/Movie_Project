package com.example.movie2.LoginService;

import com.example.movie2.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;



    @Override
    public int Login_join(LoginVO vo) {
        return loginMapper.Login_join(vo);
    }
}
