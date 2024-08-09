package com.example.movie.controller;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("movie/Reservation")
public class ReservationController {

    @Autowired
    @Qualifier("reservationService")
    public ReservationService reservationService;


//예약화면

    @GetMapping("/reservation")
    public String reservations() {

        return "movie/Reservation/reservation";
    }

    @PostMapping("/reservation_resist")
    public String reservation_resist(ReservationVO vo, RedirectAttributes ra) {
        int result = reservationService.resist(vo);
        if (result == 1) {
            ra.addFlashAttribute("잘 전송되었습니다.");
        } else {
            ra.addFlashAttribute("잘 전송되지 않았습니다");


        }

        return null;
    }
}
