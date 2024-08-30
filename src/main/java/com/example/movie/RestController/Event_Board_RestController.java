package com.example.movie.RestController;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.EventVO;
import com.example.movie.communityEventService.EventService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController

public class Event_Board_RestController {

    @Autowired
    @Qualifier("EventService")
    private EventService eventService;


//    @PostMapping("/get_count")
//    public ResponseEntity <ArrayList<EventVO>> counts(@Param("free_number") Integer free_number) {
//        EventVO success = eventService.getcounttwo(free_number);
//
//        if (success == 1) {
//            return new ResponseEntity<>(HttpStatus.OK); // 수정된 객체 반환
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 업데이트 실패 시
//        }
//    }




    }
