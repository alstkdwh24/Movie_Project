package com.example.movie.ChatService;

import com.example.movie.commandVO.ChatVO;
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
}
