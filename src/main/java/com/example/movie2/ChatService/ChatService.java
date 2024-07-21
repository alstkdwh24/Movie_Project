package com.example.movie2.ChatService;

import com.example.movie2.commandVO.ChatVO;
import com.example.movie2.util.Criteria;

import java.util.ArrayList;

public interface ChatService {
    int Question_writer(ChatVO vo);
    ArrayList<ChatVO> Question_show(Criteria cri);
    int Question_total(Criteria cri);
}
