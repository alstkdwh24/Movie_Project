package com.example.movie.RestController;

import com.example.movie.commandVO.CategoryVO;
import com.example.movie.commandVO.EventVO;
import com.example.movie.communityEventService.EventService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController

public class Event_Board_RestController {

    @Autowired
    @Qualifier("EventService")
    private EventService eventService;





    }
