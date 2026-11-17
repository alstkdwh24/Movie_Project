package com.example.movie.movie_image_service;

import com.example.movie.entity.mains.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ImageMapper {
    int event_resist(EventBoardEntity entity);

    int movie_resist(MovieEntity entity);
    ArrayList<MovieEntity> movie_resist_list(MovieEntity entity);

    int event_board_resist(EventBoardEntity entity);

    ArrayList<EventBoardEntity> get_event_list(EventBoardEntity entity);

    int gifticon_resist(GifticonEntity entity);

    ArrayList<GifticonEntity> gifticon_select(GifticonEntity entity);

    int movie_ticket_resist(TicketEntity entity);

    ArrayList<TicketEntity> movie_ticket_resist_list(TicketEntity entity);

    int DeliciousVO_image(DeliciousEntity entity);

    ArrayList<DeliciousEntity> deliciousVO_list(DeliciousEntity entity);

    ArrayList<DeliciousEntity> deliciousVO_two_list(DeliciousEntity entity);

    ArrayList<GifticonEntity> Movie_gifticon_list(GifticonEntity entity);

    ArrayList<TicketEntity> movie_ticketVOS_list(TicketEntity entity);
}
