package com.example.movie.RestController;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.EventVO;
import com.example.movie.commandVO.G_CommentVO;
import com.example.movie.communityEventService.EventService;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
//@RequestMapping("/movie/community")
public class Event_Board_RestController {

    @Autowired
    @Qualifier("EventService")
    private EventService eventService;

    //자유 게시판 댓글 기능

    @GetMapping("/get_comment")
    public ResponseEntity<ArrayList<EventVO>> get_comment(@RequestParam Integer free_number) {
        ArrayList<EventVO> comments = eventService.get_comment(free_number);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/g_comment")
    public ResponseEntity<ArrayList<G_CommentVO>> G_Comment(@RequestParam Integer g_number){
        ArrayList<G_CommentVO> vo=eventService.G_comment_show(g_number);
        return new ResponseEntity<>(vo,HttpStatus.OK);
    }




//공지사항 조회수 기능
    @PostMapping("/movie/community/g_board_count")
    public ResponseEntity<Integer> g_board_count(@RequestBody Map<String, Integer> request) {
        Integer gNumber = request.get("g_number");
        int g_board_count = eventService.g_board_count(gNumber);

        return ResponseEntity.ok(g_board_count);


    }


    //자유게시판 댓글 쓰기 기능
    @PostMapping("/free_board_comments")
    public ResponseEntity<EventVO> Post_comment(@RequestBody EventVO vo) {
        int savecomment = eventService.Post_comment(vo);
        if (savecomment == 1) {
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(vo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/g_resist")
    public ResponseEntity<G_CommentVO>G_Comment(@RequestBody G_CommentVO vo){
        int G_number=eventService.G_Comment(vo);
        if(G_number==1){
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}