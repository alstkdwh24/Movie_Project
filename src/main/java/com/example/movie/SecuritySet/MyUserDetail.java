package com.example.movie.SecuritySet;

import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetail implements UserDetails {


    private final LoginVO VO;

    public MyUserDetail(LoginVO vo) {
        this.VO = vo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list= new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return VO.getRoles();
            }
        });

        return list;
    }

    @Override
    public String getPassword() {

        return VO.getPw(); // 암호화된 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return VO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
