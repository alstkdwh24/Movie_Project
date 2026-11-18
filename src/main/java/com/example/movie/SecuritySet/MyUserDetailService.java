package com.example.movie.SecuritySet;

import com.example.movie.LoginService.LoginMapper;
import com.example.movie.dto.LoginDTO;
import com.example.movie.entity.LoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("사용자가 로그인을 시도함: " + username);

        LoginEntity entity = loginMapper.login(username);

        if (entity != null) {
            return new MyUserDetail(LoginDTO.fromEntity(entity)); // 사용자 정보를 MyUserDetail로 변환
        } else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
    }
}
