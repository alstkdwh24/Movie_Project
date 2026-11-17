package com.example.movie.movie_image_service;

import com.example.movie.dto.mains.*;
import com.example.movie.entity.mains.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service("movie_Image_Service")
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper movieImageMapper;

    @Override
    public int event_resist(EventBoardDTO dto) {
        return movieImageMapper.event_resist(dto != null ? dto.toEntity() : new EventBoardEntity());
    }

    @Override
    public int movie_resist(MovieDTO dto) {
        return movieImageMapper.movie_resist(dto != null ? dto.toEntity() : new MovieEntity());
    }

    @Override
    public ArrayList<MovieDTO> movie_resist_list(MovieDTO dto) {
        ArrayList<MovieEntity> entities = movieImageMapper.movie_resist_list(dto != null ? dto.toEntity() : new MovieEntity());
        return entities.stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int event_board_resist(EventBoardDTO dto) {
        return movieImageMapper.event_board_resist(dto != null ? dto.toEntity() : new EventBoardEntity());
    }

    @Override
    public ArrayList<EventBoardDTO> get_event_list(EventBoardDTO dto) {
        ArrayList<EventBoardEntity> entities = movieImageMapper.get_event_list(dto != null ? dto.toEntity() : new EventBoardEntity());
        return entities.stream()
                .map(EventBoardDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int gifticon_resist(GifticonDTO dto) {
        return movieImageMapper.gifticon_resist(dto != null ? dto.toEntity() : new GifticonEntity());
    }

    @Override
    public ArrayList<GifticonDTO> gifticon_select(GifticonDTO dto) {
        ArrayList<GifticonEntity> entities = movieImageMapper.gifticon_select(dto != null ? dto.toEntity() : new GifticonEntity());
        return entities.stream()
                .map(GifticonDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int movie_ticket_resist(TicketDTO dto) {
        return movieImageMapper.movie_ticket_resist(dto != null ? dto.toEntity() : new TicketEntity());
    }

    @Override
    public ArrayList<TicketDTO> movie_ticket_resist_list(TicketDTO dto) {
        ArrayList<TicketEntity> entities = movieImageMapper.movie_ticket_resist_list(dto != null ? dto.toEntity() : new TicketEntity());
        return entities.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int DeliciousVO_image(DeliciousDTO dto) {
        return movieImageMapper.DeliciousVO_image(dto != null ? dto.toEntity() : new DeliciousEntity());
    }

    @Override
    public ArrayList<DeliciousDTO> deliciousVO_list(DeliciousDTO dto) {
        ArrayList<DeliciousEntity> entities = movieImageMapper.deliciousVO_list(dto != null ? dto.toEntity() : new DeliciousEntity());
        return entities.stream()
                .map(DeliciousDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<DeliciousDTO> deliciousVO_two_list(DeliciousDTO dto) {
        ArrayList<DeliciousEntity> entities = movieImageMapper.deliciousVO_two_list(dto != null ? dto.toEntity() : new DeliciousEntity());
        return entities.stream()
                .map(DeliciousDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<GifticonDTO> Movie_gifticon_list(GifticonDTO dto) {
        ArrayList<GifticonEntity> entities = movieImageMapper.Movie_gifticon_list(dto != null ? dto.toEntity() : new GifticonEntity());
        return entities.stream()
                .map(GifticonDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<TicketDTO> movie_ticketVOS_list(TicketDTO dto) {
        ArrayList<TicketEntity> entities = movieImageMapper.movie_ticketVOS_list(dto != null ? dto.toEntity() : new TicketEntity());
        return entities.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
