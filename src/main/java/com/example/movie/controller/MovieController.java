package com.example.movie.controller;

import com.example.movie.MovieService.MovieService;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/movie")
public class MovieController  {

    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_image_service;

    @Autowired
    @Qualifier("movieService")
    public MovieService movieService;

    @GetMapping("/mains")
    public String mains(HttpSession session, Model model, HttpSession session2, Model model2, MovieVO vo) {
        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        // 세션에서 사용자 정보를 가져옵니다.
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        ArrayList<MovieVO> movie_list=movie_image_service.movie_resist_list(vo);
        StringBuilder html=new StringBuilder();
        for(MovieVO movie:movie_list){
            String uuid = movie.getUuid();
            String imageUrl = movie.getMovie_resist_filePath(); // URL 생성
            html.append("<img src='").append(imageUrl).append("' alt='").append("' />");
        }
        model.addAttribute("movieHtml",html.toString());

        System.out.println("movie_list" + movie_list);
        model2.addAttribute("movie_list",movie_list);
        return "movie/mains";
    }
}
