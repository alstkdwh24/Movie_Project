package com.example.movie.controller;

import com.example.movie.ShopService.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("movie/shops")
public class StoreController {

    @Autowired
    @Qualifier("shopService")
    public ShopService shopService;

    @GetMapping("/popcorn_store")
    public String movie_store(HttpSession session, Model model,HttpSession session2){
        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/shops/popcorn_store";
    }
}
