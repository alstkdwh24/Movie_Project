package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/movie/write_resist")
public class WriterController {


    @GetMapping("/event_resist")
    public String event_resist(HttpSession session2, Model model) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);


        return "/movie/write_resist/event_resist";
    }

    @GetMapping("/movie_resist")
    public String movie_resist(HttpSession session2, Model model) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);

        return "/movie/write_resist/movie_resist";
    }

    @GetMapping("/popcorn_resist")
    public String popcorn_resist(HttpSession session2, Model model) {
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);

        return "/movie/write_resist/popcorn_resist";
    }

    @GetMapping("/delicious_resist")
    public String delicious_resist(HttpSession session2, Model model){
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);

        return "/movie/write_resist/delicious_resist";
    }

    @GetMapping("/movie_gift_card_resist")
    public String movie_gift_card_resist(HttpSession session2, Model model){
        String roles = (String) session2.getAttribute("roles");
        model.addAttribute("roles", roles);

        return "/movie/write_resist/movie_gift_card_resist";
    }
}
