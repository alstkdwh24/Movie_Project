package com.example.movie.controller;

import com.example.movie.ShopService.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("movie/shop")
public class StoreController {

    @Autowired
    @Qualifier("shopService")
    public ShopService shopService;

    @GetMapping("/movie_store")
    public String movie_store(){
        return "movie/shop/movie_store";
    }
}
