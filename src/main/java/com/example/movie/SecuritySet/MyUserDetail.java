package com.example.movie.SecuritySet;

import com.example.movie.dto.LoginDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetail implements UserDetails {

    private final LoginDTO dto;

    public MyUserDetail(LoginDTO dto) {
        this.dto = dto;
    }

    public String getrole() {
        return dto.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                String role = dto.getRoles();
                if (role != null && !role.startsWith("ROLE_")) {
                    return "ROLE_" + role;
                }
                return role;
            }
        });
        return list;
    }

    @Override
    public String getPassword() {
        return dto.getPw(); // 암호화된 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return dto.getUsername();
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
