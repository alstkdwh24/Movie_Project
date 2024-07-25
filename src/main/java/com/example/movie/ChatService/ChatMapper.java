package com.example.movie.ChatService;


import com.example.movie.commandVO.ChatVO;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ChatMapper {
    int Question_writer(ChatVO vo);
    int Question_total(Criteria cri);
    ArrayList<ChatVO> Question_show(Criteria cri);

}
