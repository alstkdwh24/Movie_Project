package com.example.movie.movie_image_service;

import com.example.movie.dto.mains.*;

import java.util.ArrayList;

public interface ImageService {
    int event_resist(EventBoardDTO dto);

    int movie_resist(MovieDTO dto);

    ArrayList<MovieDTO> movie_resist_list(MovieDTO dto);

    int event_board_resist(EventBoardDTO dto);

    ArrayList<EventBoardDTO> get_event_list(EventBoardDTO dto);

    int gifticon_resist(GifticonDTO dto);

    ArrayList<GifticonDTO> gifticon_select(GifticonDTO dto);

    int movie_ticket_resist(TicketDTO dto);

    ArrayList<TicketDTO> movie_ticket_resist_list(TicketDTO dto);

    int DeliciousVO_image(DeliciousDTO dto);

    ArrayList<DeliciousDTO> deliciousVO_list(DeliciousDTO dto);

    ArrayList<DeliciousDTO> deliciousVO_two_list(DeliciousDTO dto);

    ArrayList<GifticonDTO> Movie_gifticon_list(GifticonDTO dto);

    ArrayList<TicketDTO> movie_ticketVOS_list(TicketDTO dto);
}
