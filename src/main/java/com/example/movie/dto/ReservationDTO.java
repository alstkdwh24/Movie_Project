package com.example.movie.dto;

import com.example.movie.entity.ReservationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDTO {
    private String username;
    private String movieTitle;
    private String movieTime;
    private String moviePlace;
    private Integer reservation_number;
    private String movieSeat;
    private String movie_title;
    private String movie_place;
    private String movie_time;
    private String movie_Seat;
    private String reservation_price;
    private String UserPhone;
    private String paymentId;

    public ReservationEntity toEntity() {
        return ReservationEntity.builder()
                .username(this.username)
                .movieTitle(this.movieTitle)
                .movieTime(this.movieTime)
                .moviePlace(this.moviePlace)
                .reservation_number(this.reservation_number)
                .movieSeat(this.movieSeat)
                .movie_title(this.movie_title)
                .movie_place(this.movie_place)
                .movie_time(this.movie_time)
                .movie_Seat(this.movie_Seat)
                .reservation_price(this.reservation_price)
                .UserPhone(this.UserPhone)
                .paymentId(this.paymentId)
                .build();
    }

    public static ReservationDTO fromEntity(ReservationEntity entity) {
        if (entity == null) return null;
        return ReservationDTO.builder()
                .username(entity.getUsername())
                .movieTitle(entity.getMovieTitle())
                .movieTime(entity.getMovieTime())
                .moviePlace(entity.getMoviePlace())
                .reservation_number(entity.getReservation_number())
                .movieSeat(entity.getMovieSeat())
                .movie_title(entity.getMovie_title())
                .movie_place(entity.getMovie_place())
                .movie_time(entity.getMovie_time())
                .movie_Seat(entity.getMovie_Seat())
                .reservation_price(entity.getReservation_price())
                .UserPhone(entity.getUserPhone())
                .paymentId(entity.getPaymentId())
                .build();
    }
}
