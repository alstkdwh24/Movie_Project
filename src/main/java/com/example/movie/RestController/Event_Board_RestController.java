package com.example.movie.RestController;

import com.example.movie.communityEventService.EventService;
import com.example.movie.dto.EventDTO;
import com.example.movie.dto.GCommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class Event_Board_RestController {

    @Autowired
    @Qualifier("EventService")
    private EventService eventService;

    // 자유 게시판 댓글 기능
    @GetMapping("/get_comment")
    public ResponseEntity<ArrayList<EventDTO>> get_comment(@RequestParam Integer free_number) {
        ArrayList<EventDTO> comments = eventService.get_comment(free_number);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/g_comment")
    public ResponseEntity<ArrayList<GCommentDTO>> G_Comment(@RequestParam Integer g_number) {
        ArrayList<GCommentDTO> vo = eventService.G_comment_show(g_number);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    // 공지사항 조회수 기능
    @PostMapping("/movie/community/g_board_count")
    public ResponseEntity<Integer> g_board_count(@RequestBody Map<String, Integer> request) {
        Integer gNumber = request.get("g_number");
        int g_board_count = eventService.g_board_count(gNumber);
        return ResponseEntity.ok(g_board_count);
    }

    // 자유게시판 댓글 쓰기 기능
    @PostMapping("/free_board_comments")
    public ResponseEntity<EventDTO> Post_comment(@RequestBody EventDTO vo) {
        int savecomment = eventService.Post_comment(vo);
        if (savecomment == 1) {
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(vo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/g_resist")
    public ResponseEntity<GCommentDTO> G_Comment(@RequestBody GCommentDTO vo) {
        int G_number = eventService.G_Comment(vo);
        if (G_number == 1) {
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(vo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
