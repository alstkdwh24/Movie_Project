package com.example.movie.controller;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("movie/Reservation")
public class ReservationController {

    @Autowired
    @Qualifier("reservationService")
    public ReservationService reservationService;


//예약화면

    @GetMapping("/reservation")
    public String reservations(HttpSession session, Model model) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/Reservation/reservation";
    }
    @GetMapping("/reservation_chair")
    public String reservation_chair(HttpSession session, Model model){
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        return "movie/Reservation/reservation_chair";
    }


}
