package com.example.movie.RestController;

import com.example.movie.ReservationService.ReservationService;
import com.example.movie.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationApi {

    @Autowired
    @Qualifier("reservationService")
    private ReservationService reservationService;

    @GetMapping("/Reservation_Seat")
    public String Reservation_Seat() {
        return null;
    }

    @PostMapping("/movie/Reservation/reservation_Delete")
    public ResponseEntity<Void> reservation_Delete(@RequestBody ReservationDTO vo) {
        reservationService.reservation_Delete(vo);
        return ResponseEntity.noContent().build();
    }
}
