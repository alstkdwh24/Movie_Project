package com.example.movie.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.movie.LoginService.LoginService;
import com.example.movie.commandVO.EventVO;
import com.example.movie.commandVO.LoginVO;
import com.example.movie.communityEventService.EventService;
import com.example.movie.util.Criteria;
import com.example.movie.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movie/community")
public class EventController {
    @Autowired
    @Qualifier("EventService")
    public EventService eventService;

    @Autowired
    @Qualifier("loginService")
    public LoginService loginService;

    @GetMapping("/event")
    public String Event2() {
        return "movie/community/event";
    }

    @GetMapping("/gboard")
    public String gboard(HttpServletRequest request, Model model, Criteria cri) {
//        String sessionUsername=(String) session.getAttribute("username");
        ArrayList<EventVO> List = eventService.gallery_g_show(cri);
        int total = eventService.gallery_g_total(cri);
        PageVO pageVO = new PageVO(cri, total);
        model.addAttribute("list", List);
        model.addAttribute("pageVO", pageVO);
        System.out.println(pageVO);

        return "movie/community/gboard";
    }

    @GetMapping("/freeboard")
    public String FREEBOARD(HttpServletRequest request, Model model, Criteria cri /*@RequestParam("free_number") Integer free_number*/) {
        ArrayList<EventVO> List = eventService.gallery_free_show(cri);
        int total = eventService.gallery_free_total(cri);
        PageVO pageVO = new PageVO(cri, total);
        model.addAttribute("list", List);
        model.addAttribute("pageVO", pageVO);
        System.out.println(pageVO);


        return "movie/community/freeboard";
    }

    @GetMapping("/g_board_writer")
 @PreAuthorize("hasRole('ROLE_1')")
    public String g_board(HttpSession session, Model model) {

//


        return "movie/community/g_board_writer";
    }


    @GetMapping("/g_board_writer_update")
    public String g_update() {
        return "movie/community/g_board_writer_update";
    }

    @GetMapping("/free_board_writer_update")
    public String free_update() {
        return "movie/community/free_board_writer_update";
    }

    @GetMapping("/free_board_writer")
//       @PreAuthorize("hasRole('ROLE_1')")
    public String freeBoardWriter(Model model) {

        return "movie/community/free_board_writer"; // 해당 뷰 반환
         }

        @GetMapping("/free_detail")
        public String free_detail (@RequestParam("free_number") Integer free_number, Model model){

            EventVO vo = eventService.freeselect(free_number);
            model.addAttribute("vo", vo);

            return "movie/community/free_detail";
        }

        @GetMapping("/g_detail")

        public String g_detail (@RequestParam("g_number") Integer g_number, Model model){

            EventVO vo = eventService.gSelect(g_number);
            model.addAttribute("vo", vo);
            return "movie/community/g_detail";
        }


        @GetMapping("/free_detail_update")
        public String free_detail_updateint (@RequestParam("free_number") Integer free_number, Model
        model, RedirectAttributes ra){

            EventVO vo = eventService.free_detail_update_select(free_number);
            model.addAttribute("vo", vo);

            return "movie/community/free_board_writer_update";
        }

        @GetMapping("/g_update")
        public String g_update (@RequestParam("g_number") Integer g_number, Model model){

            EventVO vo = eventService.g_update(g_number);
            model.addAttribute("vo", vo);
            return "movie/community/g_board_writer_update";
        }


        @PostMapping("/Gallery_free_board")
        public String Gallery_free_board (EventVO vo, RedirectAttributes ra){
            int result = eventService.gallery_free_board(vo);
            if (result == 1) {
                ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
                System.out.println(vo.getFree_title());
            } else {
                ra.addFlashAttribute("msg", "음, 이건 아니에요.");
            }
            return "redirect:/movie/community/freeboard";

        }
        @PostMapping("/Gallery_g_board")
        public String Gallery_g_board (EventVO vo, RedirectAttributes ra){
            int result = eventService.gallery_g_board(vo);
            if (result == 1) {
                ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
                System.out.println(vo.getG_title());
            } else {
                ra.addFlashAttribute("msg", "음, 이건 아니에요.");
            }
            return "redirect:/movie/community/gboard";
        }

        @GetMapping("/talk_button_cancel")
        public String talk_button_cancel (@RequestParam("free_number") Integer free_number, Model
        model, RedirectAttributes ra){
            eventService.free_delete(free_number);


            return "redirect:/movie/community/freeboard";

        }


        @PostMapping("/g_delete")
        public String g_delete (@RequestParam("g_number") Integer g_number){
            eventService.g_delete(g_number);
            return "redirect:/movie/community/gboard";
        }

        @GetMapping("/name")
        public @ResponseBody String an (HttpSession session){
            String username = (String) session.getAttribute("username");
            if (username != null) {
                return "세셔은 살아있다.";
            } else {
                return "세션살려";
            }
        }

    }

