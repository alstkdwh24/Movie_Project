package com.example.movie.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.movie.commandVO.EventVO;
import com.example.movie.communityEventService.EventService;
import com.example.movie.util.Criteria;
import com.example.movie.util.PageVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

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
    public String gboard(HttpServletRequest request, Model model, Criteria cri){
        ArrayList<EventVO> List =eventService.gallery_g_show(cri);
        int total = eventService.gallery_g_total(cri);
        PageVO pageVO=new PageVO(cri, total);
        model.addAttribute("list",List);
        model.addAttribute("pageVO",pageVO);
        System.out.println(pageVO);

        return "movie/community/gboard";
    }
    @GetMapping("/freeboard")
    public String FREEBOARD(HttpServletRequest request, Model model, Criteria cri /*@RequestParam("free_number") Integer free_number*/){
        ArrayList<EventVO> List =eventService.gallery_free_show(cri);
        int total = eventService.gallery_free_total(cri);
        PageVO pageVO=new PageVO(cri, total);
        model.addAttribute("list",List);
        model.addAttribute("pageVO",pageVO);
        System.out.println(pageVO);


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
    @PostMapping("/Gallery_g_board")
    public String Gallery_g_board(EventVO vo, RedirectAttributes ra){
        int result = eventService.gallery_g_board(vo);
        if(result ==1){
            ra.addFlashAttribute("msg","정상적으로 처리하였습니다.");
            System.out.println(vo.getG_title());
        }else{
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }
        return "redirect:/movie/community/gboard";

    }

}
