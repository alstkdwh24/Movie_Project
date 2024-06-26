package com.example.movie.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.movie.commandVO.EventVO;
import com.example.movie.communityEventService.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie/community")
public class EventController {
    @Autowired
    @Qualifier("EventService")
    public EventService eventService;

    @GetMapping("/event")
    public String Event2(){
        return "movie/community/event";
    }
    @GetMapping("/gboard")
    public String gboard(){
        return "movie/community/gboard";
    }
    @GetMapping("/freeboard")
    public String FREEBOARD(){
        return "movie/community/freeboard";
    }

    @GetMapping("/g_board_writer")
    public  String  g_board(){
        return "movie/community/g_board_writer";
    }

    @GetMapping("/free_board_writer")
    public  String free_board_writer(){
        return "movie/community/free_board_writer";
    }


    @PostMapping("/Gallery_free_board")
    public String Gallery_free_board(EventVO vo, RedirectAttributes ra){
        int result = eventService.gallery_free_board(vo);
        if(result ==1){
            ra.addFlashAttribute("msg","정상적으로 처리하였습니다.");
            System.out.println(vo.getFree_title());
        }else{
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }
   return "redirect:/movie/community/freeboard";

    }
}
