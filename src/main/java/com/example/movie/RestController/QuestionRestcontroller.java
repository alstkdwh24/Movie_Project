package com.example.movie.RestController;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.ChatVO;
import com.example.movie.commandVO.Q_CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/movie/chats")
public class QuestionRestcontroller {

    @Autowired
    @Qualifier("chatService")
    private ChatService chatService;
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
            return new ResponseEntity<>(vo, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
