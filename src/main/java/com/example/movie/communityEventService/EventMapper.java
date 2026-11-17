package com.example.movie.communityEventService;

import com.example.movie.entity.EventEntity;
import com.example.movie.entity.GCommentEntity;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface EventMapper {
    int gallery_free_board(EventEntity entity);
    ArrayList<EventEntity> gallery_free_show(Criteria cri);

    int gallery_free_total(Criteria cri);

    int gallery_g_board(EventEntity entity);

    ArrayList<EventEntity> gallery_g_show(Criteria cri);

    int gallery_g_total(Criteria cri);

    EventEntity freeselect(int free_number);

    EventEntity free_detail_update_select(int free_number);

    int findEventFree_Number(int freeNumber);

    void free_delete(int free_number);

    EventEntity gSelect(int g_number);

    void g_delete(int g_number);

    EventEntity g_update(int g_number);

    int Post_comment(EventEntity entity);

    int g_board_count(int gNumber);

    ArrayList<EventEntity> get_comment(int free_number);
    int G_Comment(GCommentEntity entity);

    ArrayList<GCommentEntity> G_comment_show(int g_number);

    int free_update_resist(EventEntity entity);

    void free_comment(int free_number);
}
