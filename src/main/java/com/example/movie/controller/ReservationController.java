package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationController {

    @Autowired
    @Qualifier("reservationService")
    public ReservationService reservationService;
    }
