package com.example.movie.ChatService;


import com.example.movie.entity.ChatEntity;
import com.example.movie.entity.QCommentEntity;
import com.example.movie.util.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ChatMapper {
    int Question_writer(ChatEntity entity);
    int Question_total(Criteria cri);
    ArrayList<ChatEntity> Question_show(Criteria cri);

    int Q_count(int QNumber);

    ChatEntity Question_detail(int QNumber);

    int Question_comment_resist(QCommentEntity entity);

    ArrayList<QCommentEntity> question_comment(int Q_number);


}
