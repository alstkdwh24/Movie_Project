package com.example.movie.Reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("reservationService")
public class ReservationserviceImpl implements ReservationService{

    @Autowired
    private ReservationMapper reservationMapper;
}
