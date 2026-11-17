package com.example.movie.communityEventService;

import com.example.movie.dto.EventDTO;
import com.example.movie.dto.GCommentDTO;
import com.example.movie.entity.EventEntity;
import com.example.movie.entity.GCommentEntity;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service("EventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Override
    public int gallery_free_board(EventDTO dto) {
        return eventMapper.gallery_free_board(dto.toEntity());
    }

    @Override
    public int gallery_free_total(Criteria cri) {
        return eventMapper.gallery_free_total(cri);
    }

    @Override
    public ArrayList<EventDTO> gallery_free_show(Criteria cri) {
        ArrayList<EventEntity> entities = eventMapper.gallery_free_show(cri);
        return entities.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int gallery_g_board(EventDTO dto) {
        return eventMapper.gallery_g_board(dto.toEntity());
    }

    @Override
    public int gallery_g_total(Criteria cri) {
        return eventMapper.gallery_g_total(cri);
    }

    @Override
    public ArrayList<EventDTO> gallery_g_show(Criteria cri) {
        ArrayList<EventEntity> entities = eventMapper.gallery_g_show(cri);
        return entities.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public EventDTO freeselect(int free_number) {
        return EventDTO.fromEntity(eventMapper.freeselect(free_number));
    }

    @Override
    public int findEventFree_Number(int freeNumber) {
        System.out.println("freeNumber:" + freeNumber);
        return eventMapper.findEventFree_Number(freeNumber);
    }

    @Override
    public EventDTO free_detail_update_select(int free_number) {
        return EventDTO.fromEntity(eventMapper.free_detail_update_select(free_number));
    }

    @Override
    public EventDTO free_delete(int free_number) {
        eventMapper.free_delete(free_number);
        return null;
    }

    @Override
    public EventDTO gSelect(int g_number) {
        return EventDTO.fromEntity(eventMapper.gSelect(g_number));
    }

    @Override
    public void g_delete(int g_number) {
        eventMapper.g_delete(g_number);
    }

    @Override
    public EventDTO g_update(int g_number) {
        return EventDTO.fromEntity(eventMapper.g_update(g_number));
    }

    @Override
    public int Post_comment(EventDTO dto) {
        return eventMapper.Post_comment(dto.toEntity());
    }

    @Override
    public int g_board_count(int gNumber) {
        System.out.println("freeNumber:" + gNumber);
        return eventMapper.g_board_count(gNumber);
    }

    // 자유 게시판 댓글 기능
    @Override
    public ArrayList<EventDTO> get_comment(int free_number) {
        ArrayList<EventEntity> entities = eventMapper.get_comment(free_number);
        return entities.stream()
                .map(EventDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int G_Comment(GCommentDTO dto) {
        return eventMapper.G_Comment(dto.toEntity());
    }

    @Override
    public ArrayList<GCommentDTO> G_comment_show(int g_number) {
        ArrayList<GCommentEntity> entities = eventMapper.G_comment_show(g_number);
        return entities.stream()
                .map(GCommentDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public int free_update_resist(EventDTO dto) {
        return eventMapper.free_update_resist(dto.toEntity());
    }

    // 자유게시판 댓글제거
    @Override
    public EventDTO free_comment(int free_number) {
        eventMapper.free_comment(free_number);
        return null;
    }
}
