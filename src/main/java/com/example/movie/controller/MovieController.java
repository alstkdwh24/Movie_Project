package com.example.movie.controller;

import com.example.movie.ImageProperties.ImageProperties;
import com.example.movie.MovieService.MovieService;
import com.example.movie.dto.mains.MovieDTO;
import com.example.movie.movie_image_service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    @Qualifier("movie_Image_Service")
    private ImageService movie_image_service;

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

    @GetMapping("/mains")
    @CrossOrigin(origins = "http://15.164.7.12:9494")
    public ModelAndView movieVOResponseEntity(HttpSession session, Model model, HttpSession session2, MovieDTO vo) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        ArrayList<MovieDTO> movie_list = movie_image_service.movie_resist_list(vo);
        model.addAttribute("movie_list", movie_list);

        for (int i = 0; i < movie_list.toArray().length; i++) {
            String firstFile = movie_list.get(i).getFilePath() + "/" + movie_list.get(i).getMovie_filename();
            filenames = movie_list.get(i).getFilePath() + "/" + movie_list.get(i).getMovie_filename();

            System.out.println("filenames2:" + filenames);
            session.setAttribute("firstFile", firstFile);
            model.addAttribute("staticImageUrls", filenames);
        }

        session.setAttribute("movie_list", movie_list);

        ModelAndView modelAndView = new ModelAndView("movie/mains");

        String movieHtml = generateMovieHtml(movie_list);
        modelAndView.addObject("movieHtml", movieHtml);

        return modelAndView;
    }

    private String generateMovieHtml(ArrayList<MovieDTO> movie_list) {
        StringBuilder html = new StringBuilder();

        for (int i = 0; i < movie_list.size(); i++) {
            MovieDTO movieVO = movie_list.get(i);
            String imageUrl = movieVO.getUploadPaths();
            String movieTitle = movieVO.getMovie_title();
            System.out.println("imageUrl: " + imageUrl);

            String filename = movieVO.getFilePath() + "/" + movieVO.getMovie_filename();
            System.out.println("staticImageUrl2" + filename);

            html.append("<div class=\"movie_picture\">")
                    .append("<div class=\"movie_image\">")
                    .append("<div class='movie_movie'>")
                    .append("<div id=\"'movie_real_image'\"").append("class='movie_real_image'>")
                    .append("</div>")
                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"movie_gle\">")
                    .append(movieTitle)
                    .append("</div>")
                    .append("</div>");
        }

        return html.toString();
    }
}
