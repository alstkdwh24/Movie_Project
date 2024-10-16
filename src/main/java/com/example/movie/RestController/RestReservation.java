package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.commandVO.ReservationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class RestReservation {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;
    @GetMapping("/Reservation_Seat")
    public String Reservation_Seat(){
return null;
    }

    @PostMapping("/movie/Reservation/reservation_Delete")
    public ResponseEntity<Void> reservation_Delete(@RequestBody ReservationVO vo){
        reservationService.reservation_Delete(vo);
        return ResponseEntity.noContent().build();
    }
}

