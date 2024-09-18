package com.example.movie.ChatService;

import com.example.movie.commandVO.ChatVO;
import com.example.movie.commandVO.EventVO;
import com.example.movie.commandVO.Q_CommentVO;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("chatService")

public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatMapper chatMapper;

    @Override
    public int Question_writer(ChatVO vo) {
        return chatMapper.Question_writer(vo);
    }

    @Override
    public ArrayList<ChatVO> Question_show(Criteria cri) {
        return chatMapper.Question_show(cri);
    }

    @Override
    public int Question_total(Criteria cri) {
        return chatMapper.Question_total(cri);
    }

    @Override
    public int Q_count(int QNumber) {
        return chatMapper.Q_count(QNumber);
    }

    @Override
    public ChatVO Question_detail(int QNumber) {
        return chatMapper.Question_detail(QNumber);
    }

    @Override
    public int Question_comment_resist(Q_CommentVO vo) {
        return chatMapper.Question_comment_resist(vo);
    }
}
