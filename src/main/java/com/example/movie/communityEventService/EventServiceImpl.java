package com.example.movie.communityEventService;

import com.example.movie.commandVO.EventVO;
import com.example.movie.commandVO.G_CommentVO;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("EventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventMapper eventMapper;

    public int gallery_free_board(EventVO vo) {
        return eventMapper.gallery_free_board(vo);
    }

    @Override
    public int gallery_free_total(Criteria cri) {
        return eventMapper.gallery_free_total(cri);
    }

    @Override
    public ArrayList<EventVO> gallery_free_show(Criteria cri) {


        return eventMapper.gallery_free_show(cri);
    }




    public int gallery_g_board(EventVO vo) {
        return eventMapper.gallery_g_board(vo);
    }

    @Override
    public int gallery_g_total(Criteria cri) {
        return eventMapper.gallery_g_total(cri);
    }

    @Override
    public ArrayList<EventVO> gallery_g_show(Criteria cri) {
        return eventMapper.gallery_g_show(cri);
    }

    @Override
    public EventVO freeselect(int free_number) {
        return eventMapper.freeselect(free_number);
    }

    @Override
    public int findEventFree_Number(int freeNumber) {
        System.out.println("freeNumber:" + freeNumber);
        return eventMapper.findEventFree_Number(freeNumber);
    }

    @Override
    public EventVO free_detail_update_select(int free_number) {
        return eventMapper.free_detail_update_select(free_number);
    }

    @Override
    public void free_delete(int free_number) {
        eventMapper.free_delete(free_number);
    }

    @Override
    public EventVO gSelect(int g_number) {
        return eventMapper.gSelect(g_number);
    }

    @Override
    public void g_delete(int g_number) {
        eventMapper.g_delete(g_number);
    }

    @Override
    public EventVO g_update(int g_number) {
        return eventMapper.g_update(g_number);
    }

    @Override
    public int Post_comment(EventVO vo) {
        return eventMapper.Post_comment(vo);
    }

    @Override
    public int g_board_count(int gNumber) {
        System.out.println("freeNumber:" + gNumber);

        return eventMapper.g_board_count(gNumber);
    }
//자유 게시판 댓글 기능
    @Override
    public ArrayList<EventVO> get_comment(int free_number) {
        return eventMapper.get_comment(free_number);
    }

    @Override
    public int G_Comment(G_CommentVO vo) {
        return eventMapper.G_Comment(vo);
    }

    @Override
    public ArrayList<G_CommentVO> G_comment_show(int g_number) {
        return eventMapper.G_comment_show(g_number);
    }


}
