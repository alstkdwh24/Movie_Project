package com.example.movie.communityEventService;

import com.example.movie.dto.EventDTO;
import com.example.movie.dto.GCommentDTO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface EventService {
    int gallery_free_board(EventDTO dto);
    int gallery_free_total(Criteria cri);
    ArrayList<EventDTO> gallery_free_show(Criteria cri);

    int gallery_g_board(EventDTO dto);
    int gallery_g_total(Criteria cri);
    ArrayList<EventDTO> gallery_g_show(Criteria cri);

    EventDTO freeselect(int free_number);

    int findEventFree_Number(int freeNumber);

    EventDTO free_detail_update_select(int free_number);

    EventDTO free_delete(int free_number);

    EventDTO gSelect(int g_number);

    void g_delete(int g_number);

    EventDTO g_update(int g_number);

    // 댓글 등록
    int Post_comment(EventDTO dto);

    int g_board_count(int gNumber);

    // 자유 게시판 댓글 기능
    ArrayList<EventDTO> get_comment(int free_number);

    int G_Comment(GCommentDTO dto);

    ArrayList<GCommentDTO> G_comment_show(int g_number);

    int free_update_resist(EventDTO dto);

    // 댓글 제거
    EventDTO free_comment(int free_number);
}
