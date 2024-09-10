package com.example.movie.RestController;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.EventVO;
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



    @PostMapping("/movie/community/g_board_count")
    public ResponseEntity<Integer> g_board_count(@RequestBody Map<String, Integer> request) {
        Integer gNumber = request.get("g_number");
        int g_board_count = eventService.g_board_count(gNumber);

        return ResponseEntity.ok(g_board_count);


    }

    @PostMapping("/free_board_comments")
    public ResponseEntity<EventVO> Post_comment(@RequestBody EventVO vo) {
        int savecomment = eventService.Post_comment(vo);
        if (savecomment == 1) {
            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(vo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}