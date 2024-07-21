package com.example.movie2.controller;

import com.example.movie2.ReservationService.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("movie/Reservation")
public class ReservationController {

    @Autowired
    @Qualifier("reservationService")
    public ReservationService reservationService;



//예약화면
    @GetMapping ("/sreservation")
    public String reservation(){

        return "movie/Reservation/sreservation";
    }
    @GetMapping("/reservation")
    public  String reser(){
        return "movie/Reservation/reservation";
    }
    }
