package com.example.movie.RestController;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.ChatVO;
import com.example.movie.commandVO.G_CommentVO;
import com.example.movie.commandVO.Q_CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
@RestController
@RequestMapping("/movie/chats")
public class Question_Restcontroller {

    @Autowired
    @Qualifier("chatService")
    private ChatService chatService;


    @GetMapping("/question_comment")
    public ResponseEntity<ArrayList<Q_CommentVO>> question_comment(@RequestParam Integer Q_number){
        ArrayList<Q_CommentVO> Q_Comment_List=chatService.question_comment(Q_number);
        return new ResponseEntity<>(Q_Comment_List, HttpStatus.OK);
    }
    @PostMapping("/Question_count")
    public ResponseEntity<Integer> Question_count(@RequestBody Map<String, Integer> request) {
        Integer QNumber = request.get("Q_number");
        int Q_number = chatService.Q_count(QNumber);
        if (Q_number == 1) {
            return ResponseEntity.ok(Q_number);

        } else {
            return null;
        }


    }
    @PostMapping("/Question_comment_resist")
    public ResponseEntity<Q_CommentVO> Question_comment_resist(@RequestBody Q_CommentVO vo){
        int Chat_for=chatService.Question_comment_resist(vo);

        if(Chat_for==1){

            return new ResponseEntity<>(vo, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
