package com.example.movie.controller;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.RestController.RestPaymentController;
import com.example.movie.commandVO.EventVO;
import com.example.movie.commandVO.ReservationVO;
import com.example.movie.commandVO.TokenVO;
import com.example.movie.util.Criteria;
import com.example.movie.util.PageVO;
import org.hibernate.cache.spi.support.StorageAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("movie/Reservation")
public class ReservationController {

    @Autowired
    @Qualifier("reservationService")
    public ReservationService reservationService;

//    public ReservationController(RestTemplate restTemplate) {
//        super(restTemplate);
//    }


//예약화면

    @GetMapping("/reservation")
    public String reservations(HttpSession session, Model model, HttpSession session2) {

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/Reservation/reservation";
    }

    @GetMapping("/reservation_chair")
    public String reservation_chair(HttpSession session, Model model, HttpSession session2) {

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        return "movie/Reservation/reservation_chair";
    }

    @GetMapping("/reservation_report")
    public String reservation_report(HttpSession session, Model model, Criteria cri, ReservationVO vo, HttpSession session2 ,HttpSession session3) {
        String getSession = (String) session2.getAttribute("accessToken");
        int total = reservationService.getReservation_board(cri);
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        cri.setUsername(userDetails.getUsername()); // 세션에서 가져온 username 사용
        List<ReservationVO> List = reservationService.getReservation_pay(cri);


        String roles= (String) session3.getAttribute("roles");
        model.addAttribute("roles",roles);
        PageVO pageVO = new PageVO(cri, total);
        model.addAttribute("userSession", userDetails);
        model.addAttribute("list", List);
        model.addAttribute("accessToken", getSession);
        System.out.println("getSession"+getSession);
        return "movie/Reservation/reservation_report";

    }

    @GetMapping("/reservation_pay")
    public String reservation_pay(HttpSession session, Model model, HttpSession session2) {


        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/Reservation/reservation_pay";
    }

    @GetMapping("success_reservation_page")
    public String success_reservation_page(HttpSession session, Model model, HttpSession session2) {

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/Reservation/success_reservation_page";
    }

}
