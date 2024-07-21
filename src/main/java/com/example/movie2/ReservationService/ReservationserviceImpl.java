package com.example.movie2.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reservationService")
public class ReservationserviceImpl implements ReservationService{

    @Autowired
    private ReservationMapper reservationMapper;
}
