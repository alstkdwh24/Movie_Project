package com.example.movie.controller;

import com.example.movie.ReservationService.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;


}
