package com.example.movie.SecuritySet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Paths;

@Configuration
@EnableWebSecurity
public class MovieConfig implements WebMvcConfigurer {

    @Value("${project.upload.path}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://15.164.7.12:9494")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String localUploadUri = Paths.get(uploadPath).toAbsolutePath().normalize().toUri().toString();
        if (!localUploadUri.endsWith("/")) {
            localUploadUri += "/";
        }
        registry.addResourceHandler("/css/uploadImage/files/**", "/upload/**")
                .addResourceLocations(localUploadUri, "classpath:/static/css/uploadImage/files/");
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();
        http.authorizeRequests((authorize) -> authorize.antMatchers("/movie").authenticated()
                .antMatchers("/movie/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/chat/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/movie/community/free_board_writer").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/community/free_board_detail").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/community/g_board_writer").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/community/g_board_detail").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/Reservation/reservation").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/Reservation/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/movie/chats/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/paymentIdVOList").hasAnyRole("USER", "ADMIN")
                .antMatchers("/payments/**").permitAll()
                .anyRequest().permitAll());

        http.formLogin()
                .loginPage("/movie/login/login")
                .loginProcessingUrl("/movie/Login_form")
                .defaultSuccessUrl("/movie/mains")
                .failureUrl("/movie/login/login")
                .failureForwardUrl("/movie/login/login");

        http.logout()
                .logoutUrl("/movie/login/logout")
                .logoutSuccessUrl("/movie/login/login")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    if (session != null) {
                        session.invalidate();
                    }
                })
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/movie/login/login");
                });

        http.sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                        HttpServletResponse response = event.getResponse();
                        response.sendRedirect("/movie/login/login");
                    }
                });
        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }
}
