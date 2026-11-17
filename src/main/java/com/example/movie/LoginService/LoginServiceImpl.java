package com.example.movie.LoginService;

import com.example.movie.dto.LoginDTO;
import com.example.movie.dto.LoginUpdateDTO;
import com.example.movie.entity.LoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public int Login_join(LoginDTO dto) {
        String password = bCryptPasswordEncoder.encode(dto.getPw());
        dto.setRoles("ROLE_USER");
        dto.setPw(password);
        return loginMapper.Login_join(dto.toEntity());
    }

    @Override
    public LoginDTO login(String username) {
        return LoginDTO.fromEntity(loginMapper.login(username));
    }

    @Override
    public ArrayList<LoginDTO> getAcount(String username) {
        ArrayList<LoginEntity> entities = loginMapper.getAcount(username);
        return entities.stream()
                .map(LoginDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // 회원정보 수정
    @Override
    public void get_Mypage_Modify(String username, LoginUpdateDTO update) {
        loginMapper.get_Mypage_Modify(username, update.toEntity());
    }
}
