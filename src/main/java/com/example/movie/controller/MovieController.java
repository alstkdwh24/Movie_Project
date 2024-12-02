package com.example.movie.controller;

import com.example.movie.ImageProperties.ImageProperties;
import com.example.movie.MovieService.MovieService;
import com.example.movie.commandVO.MainsVO.MovieVO;
import com.example.movie.movie_image_service.Movie_Image_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;
import retrofit2.http.Url;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movie")

public class MovieController {

    @Autowired
    @Qualifier("movie_Image_Service")
    private Movie_Image_Service movie_image_service;


    private final ImageProperties imageProperties;

    @Autowired
    public MovieController(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    @Autowired
    @Qualifier("movieService")
    public MovieService movieService;
    public String imageUrl;

    public String filePath;
    public String filenames;

//    @GetMapping("/mains")
//    public String mains(HttpSession session, Model model, HttpSession session2, MovieVO vo) {
//        String roles = (String) session2.getAttribute("roles");
//        model.addAttribute("roles", roles);
//        // 세션에서 사용자 정보를 가져옵니다.
//        UserDetails userDetails = (UserDetails) session.getAttribute("user");
//        model.addAttribute("userSession", userDetails);
//        // 영화 목록을 가져옵니다.
//        ArrayList<MovieVO> movie_list = movie_image_service.movie_resist_list(vo);
//
//        String movieHtml = generateMovieHtml(movie_list);
//        System.out.println("movie_list: " + movie_list);
//
//        // 세션에 영화 목록 저장 (필요한 경우에만)
//        session.setAttribute("movie_list", movie_list);
//
//        // 모델에 영화 목록과 HTML 추가
//        model.addAttribute("movie_list", movie_list);
//
//        return "movie/mains"; // 뷰 이름 반환
//    }

    @GetMapping("/mains")
    @CrossOrigin(origins = "http://localhost:9494") // 적절한 출처 설정
    public ModelAndView movieVOResponseEntity(HttpSession session, Model model, HttpSession session2, MovieVO vo) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);
        // 세션에서 사용자 정보를 가져옵니다.
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        // 영화 목록을 가져옵니다.
        ArrayList<MovieVO> movie_list = movie_image_service.movie_resist_list(vo);
        model.addAttribute("movie_list", movie_list);


        for (int i = 0; i < movie_list.toArray().length; i++) {

            String firstFile = movie_list.get(i).getFilePath() + "/" + movie_list.get(i).getMovie_filename();
            filenames = movie_list.get(i).getFilePath() + "/" + movie_list.get(i).getMovie_filename(); // 파일 이름만 사용

            System.out.println("filenames2:" + filenames);
            session.setAttribute("firstFile", firstFile);
            model.addAttribute("staticImageUrls", filenames);


        }

        // 세션에 영화 목록 저장 (필요한 경우)
        session.setAttribute("movie_list", movie_list);

        // ModelAndView 객체 생성
        ModelAndView modelAndView = new ModelAndView("/movie/mains.html"); // "movieView"는 Thymeleaf 템플릿 이름입니다.

        // HTML 생성
        String movieHtml = generateMovieHtml(movie_list);


        // 생성된 HTML을 모델에 추가
        modelAndView.addObject("movieHtml", movieHtml);



        return modelAndView; // ModelAndView 반환
    }

    private String generateMovieHtml(ArrayList<MovieVO> movie_list) {
        StringBuilder html = new StringBuilder();

        for (int i = 0; i < movie_list.size(); i++) {
            MovieVO movieVO=movie_list.get(i);
            String imageUrl = movieVO.getUploadPaths(); // 영화 파일 경로 가져오기
            String movieTitle = movieVO.getMovie_title(); // 영화 제목 가져오기
            System.out.println("imageUrl: " + imageUrl);

            // 정적 리소스 경로 설정 (필요에 따라 수정)
            String filename = movieVO.getFilePath() + "/" + movieVO.getMovie_filename(); // 파일 이름만 사용


            System.out.println("staticImageUrl2" + filename);
            // HTML 생성
            html.append("<div class=\"movie_picture\">")
                    .append("<div class=\"movie_image\">")
                    .append("<div class='movie_movie'>")
                    .append("<div id=\"'movie_real_image'\"").append("class='movie_real_image'>") // ID 설정
                    .append("</div>")
                    .append("</div>") // th:each div 종료
                    .append("</div>") // movie_image div 종료
                    .append("<div class=\"movie_gle\">")
                    .append(movieTitle) // 영화 제목 추가
                    .append("</div>")
                    .append("</div>"); // movie_picture div 종료
        }

        return html.toString(); // 생성된 HTML을 반환
    }

}
