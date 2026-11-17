package com.example.movie.ChatService;

import com.example.movie.dto.ChatDTO;
import com.example.movie.dto.QCommentDTO;
import com.example.movie.entity.ChatEntity;
import com.example.movie.entity.QCommentEntity;
import com.example.movie.util.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service("chatService")

public class ChatServiceImpl implements ChatService{

    @Autowired
    private ChatMapper chatMapper;

    @Override
    public int Question_writer(ChatDTO dto) {
        return chatMapper.Question_writer(dto.toEntity());
    }

    @Override
    public ArrayList<ChatDTO> Question_show(Criteria cri) {
        ArrayList<ChatEntity> entities = chatMapper.Question_show(cri);
        return entities.stream()
                .map(ChatDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
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
    public ChatDTO Question_detail(int QNumber) {
        return ChatDTO.fromEntity(chatMapper.Question_detail(QNumber));
    }

    @Override
    public int Question_comment_resist(QCommentDTO dto) {
        return chatMapper.Question_comment_resist(dto.toEntity());
    }

    @Override
    public ArrayList<QCommentDTO> question_comment(int Q_number) {
        ArrayList<QCommentEntity> entities = chatMapper.question_comment(Q_number);
        return entities.stream()
                .map(QCommentDTO::fromEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
