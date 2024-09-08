package com.example.movie.RestController;

import com.example.movie.ChatService.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
}
