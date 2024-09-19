package com.example.movie.ChatService;

import com.example.movie.commandVO.ChatVO;
import com.example.movie.commandVO.Q_CommentVO;
import com.example.movie.util.Criteria;

import java.util.ArrayList;

public interface ChatService {
    int Question_writer(ChatVO vo);
    ArrayList<ChatVO> Question_show(Criteria cri);
    int Question_total(Criteria cri);

    int Q_count(int QNumber);

    ChatVO Question_detail(int QNumber);

    int Question_comment_resist(Q_CommentVO vo);

    ArrayList<Q_CommentVO> question_comment(int Q_number);
}
