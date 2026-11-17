package com.example.movie.ChatService;

import com.example.movie.dto.ChatDTO;
import com.example.movie.dto.QCommentDTO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface ChatService {
    int Question_writer(ChatDTO dto);
    ArrayList<ChatDTO> Question_show(Criteria cri);
    int Question_total(Criteria cri);

    int Q_count(int QNumber);

    ChatDTO Question_detail(int QNumber);

    int Question_comment_resist(QCommentDTO dto);

    ArrayList<QCommentDTO> question_comment(int Q_number);
}
