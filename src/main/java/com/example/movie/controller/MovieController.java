package com.example.movie.controller;

import com.example.movie.MovieService.MovieService;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movie")

public class MovieController {

    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_image_service;

    @Autowired
    @Qualifier("movieService")
    public MovieService movieService;
    public String imageUrl;

    @GetMapping("/mains")
    public String mains(HttpSession session, Model model, HttpSession session2, Model model2, MovieVO vo) {
        Resource
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);

        // 세션에서 사용자 정보를 가져옵니다.
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        // 영화 목록을 가져옵니다.
        ArrayList<MovieVO> movie_list = movie_image_service.movie_resist_list(vo);
        StringBuilder html = new StringBuilder();
        for (MovieVO movieVO : movie_list) {
            String imageUrl = movieVO.getMovie_resist_filePath();
            String movieTitle = movieVO.getMovie_title(); // 영화 제목을 가져옵니다.
            System.out.println("imageUrl: " + imageUrl);

            // HTML 생성
            html.append("<div class=\"movie_picture\"> ")
                    .append("<div class=\"movie_image\">")
                    .append("<div class=\"movie_real_image\">\n")
                    .append("<img src='/mains/image/").append(imageUrl).append("' alt='영화 이미지' />") // 이미지 URL 설정
                    .append("</div></div>")
                    .append("<div class=\"movie_gle\">").append(movieTitle).append("</div>\n")
                    .append("</div>");
        }
        String movieHtml = movieResistHtml(movie_list); // HTML 생성
        System.out.println("movie_list: " + movie_list);

        // 모델에 영화 목록과 HTML 추가
        model.addAttribute("movie_list", movie_list);
        model.addAttribute("movieHtml", movieHtml);

        return "movie/mains";
    }







    }
