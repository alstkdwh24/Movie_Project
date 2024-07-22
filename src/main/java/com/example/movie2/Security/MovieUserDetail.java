package com.example.movie2.Security;

import com.example.movie2.commandVO.LoginVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieUserDetail implements UserDetails {

    private LoginVO loginVO;

    public MovieUserDetail(LoginVO vo) {
        this.loginVO=vo;
    }

    public String getrole(){
        return loginVO.getRole();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> list=new ArrayList<>();

        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return loginVO.getRole();
            }
        });




        return list;
    }

    @Override
    public String getPassword() {
        return loginVO.getPw();
    }

    @Override
    public String getUsername() {
        return loginVO.getId();
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
