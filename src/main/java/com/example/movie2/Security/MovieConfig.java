package com.example.movie2.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //설정파일임
@EnableWebSecurity //이 설정파일을 시큐리티 필터에 등록을 시킴
public class MovieConfig {

    @Autowired
    MovieUserDetailService movieUserDetailService;


    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();


        http.authorizeRequests((authorize)->authorize.antMatchers("/movie/").authenticated()
//                .antMatchers("/user/**").hasAnyRole("tester","admin","user")
//                .antMatchers("/admin/**").hasRole("admin")
                .anyRequest().permitAll());



        http.formLogin()
                .loginPage("/movie/login/login") // 커스터마이징된 로그인 페이지 경로
                .loginProcessingUrl("/movie/login/Login_form") // 로그인 처리 URL
//                .usernameParameter("yyy") // 사용자명 파라미터 이름 설정
//                .passwordParameter("xxx"); //패스워드 파라미터 변경 시에 씋수있고
                .defaultSuccessUrl("/movie/mains");
        return http.build();

    }



}
