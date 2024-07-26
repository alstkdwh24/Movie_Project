package com.example.movie.SecuritySet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Configuration //설정파일임
@EnableWebSecurity
public class MovieConfig {
//    @Autowired
//    private MyUserDetailService userDetailService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
//public BCryptPasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//}

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests((authorize)->authorize.antMatchers("/movie").authenticated()
                .antMatchers("/movie/user/**").hasAnyRole("ROLE_1")
                .antMatchers("/movie/chat/**").hasAnyRole("ROLE_1")
                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().permitAll());




        //시큐러티 기반의 플로그인을 사용한다.
        http.formLogin()
                .loginPage("/movie/login/login") // 커스터마이징된 로그인 페이지 경로
                .loginProcessingUrl("/movie/Login_form") // 로그인 처리 URL
////                .usernameParameter("yyy") // 사용자명 파라미터 이름 설정
////                .passwordParameter("xxx"); //패스워드 파라미터 변경 시에 씋수있고
                .defaultSuccessUrl("/movie/mains")
                .failureUrl("/movie/login/login");
        http.logout()
                .logoutUrl("/movie/login/logout")
                .logoutSuccessUrl("/movie/login/login")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session= request.getSession();
                    if(session !=null){
                     session.invalidate();
                    }
                })
        .logoutSuccessHandler((request, response, authentication) -> {
            response.sendRedirect("/movie/login/login");
        });
        return http.build();
    }

}
