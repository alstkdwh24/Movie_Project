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
import java.security.Principal;
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
    public ResponseEntity<Void> reservation_Delete(@RequestBody ReservationVO vo, Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 본인의 결제 전 예약만 지운다. 남의 예약·결제 끝난 예약이면 0건 → 404
        vo.setUsername(principal.getName());
        int deleted = reservationService.reservation_Delete(vo);
        return deleted == 1 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

