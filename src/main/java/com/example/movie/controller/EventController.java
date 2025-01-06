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
import org.springframework.http.ResponseEntity;
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

    //자유게시판 CEUD

    @GetMapping("/freeboard")
    public String FREEBOARD(HttpServletRequest request, Model model, Criteria cri /*@RequestParam("free_number") Integer free_number*/, HttpSession session, HttpSession session2) {
        ArrayList<EventVO> List = eventService.gallery_free_show(cri);
        int total = eventService.gallery_free_total(cri);
        PageVO pageVO = new PageVO(cri, total);
        model.addAttribute("list", List);
        model.addAttribute("pageVO", pageVO);
        System.out.println(pageVO);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);


        return "movie/community/freeboard";
    }

    @GetMapping("/free_board_writer_update")
    public String free_update(HttpSession session, Model model, HttpSession session2) {

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);
        return "movie/community/free_board_writer_update";
    }

    @GetMapping("/free_board_writer")
//       @PreAuthorize("hasRole('ROLE_1')")
    public String freeBoardWriter(Model model, HttpSession session, HttpSession session2) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);
        return "/movie/community/free_board_writer"; // 해당 뷰 반환
    }

    @GetMapping("/free_detail")
    public String free_detail(@RequestParam("free_number") Integer free_number, Model model, HttpSession session, HttpSession session2) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        String roles= (String) session2.getAttribute("roles");
        model.addAttribute("roles",roles);
        if (userDetails == null) {
            return "redirect:/movie/login/login";
        } else {
            model.addAttribute("userSession", userDetails);
            EventVO vo = eventService.freeselect(free_number);
            model.addAttribute("vo", vo);
            return "movie/community/free_detail";
        }
    }

    @PostMapping("/Gallery_free_board")
    public String Gallery_free_board(EventVO vo, RedirectAttributes ra) {
        int result = eventService.gallery_free_board(vo);
        if (result == 1) {
            ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
            System.out.println(vo.getFree_title());
        } else {
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }
        return "redirect:/movie/community/freeboard";

    }

    @GetMapping("/talk_button_cancel")
    public String talk_button_cancel(@RequestParam("free_number") Integer free_number, Model
            model, RedirectAttributes ra) {
        eventService.free_delete(free_number);


        return "redirect:/movie/community/freeboard";

    }



    @GetMapping("/event")
    public String Event2(HttpSession session, Model model) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        String roles= (String) session.getAttribute("roles");
        model.addAttribute("roles",roles);

        return "movie/community/event";
    }

    @GetMapping("/gboard")
    public String gboard(HttpServletRequest request, Model model, Criteria cri, HttpSession session) {
//        String sessionUsername=(String) session.getAttribute("username");
        ArrayList<EventVO> List = eventService.gallery_g_show(cri);
        int total = eventService.gallery_g_total(cri);
        PageVO pageVO = new PageVO(cri, total);
        model.addAttribute("list", List);
        model.addAttribute("pageVO", pageVO);
        System.out.println(pageVO);

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);


        return "movie/community/gboard";
    }


    @PostMapping("/get_count")
    @ResponseBody
    public ResponseEntity<Integer> getCount(@RequestBody Map<String, Integer> request) {
        Integer freeNumber = request.get("free_number");
        // 카운트 로직을 여기에 추가

        int count = eventService.findEventFree_Number(freeNumber);
        System.out.println("count" + count);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/g_board_writer")
    @PreAuthorize("hasRole('ROLE_1')")
    public String g_board(HttpSession session, Model model) {


        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
//


        return "movie/community/g_board_writer";
    }


    @GetMapping("/g_board_writer_update")
    public String g_update(HttpSession session, Model model) {

        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/community/g_board_writer_update";
    }






    @GetMapping("/g_detail")
    public String g_detail(@RequestParam("g_number") Integer g_number, Model model, HttpSession session) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        EventVO vo = eventService.gSelect(g_number);
        model.addAttribute("vo", vo);
        return "movie/community/g_detail";
    }


    @GetMapping("/free_detail_update")
    public String free_detail_updateint(@RequestParam("free_number") Integer free_number, Model
            model, RedirectAttributes ra, HttpSession session) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        EventVO vo = eventService.free_detail_update_select(free_number);
        model.addAttribute("vo", vo);

        return "movie/community/free_board_writer_update";
    }

    @GetMapping("/g_update")
    public String g_update(@RequestParam("g_number") Integer g_number, Model model, HttpSession session) {

        EventVO vo = eventService.g_update(g_number);
        model.addAttribute("vo", vo);


        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/community/g_board_writer_update";
    }




    @PostMapping("/Gallery_g_board")
    public String Gallery_g_board(EventVO vo, RedirectAttributes ra) {
        int result = eventService.gallery_g_board(vo);
        if (result == 1) {
            ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
            System.out.println(vo.getG_title());
        } else {
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }
        return "redirect:/movie/community/gboard";
    }




    @PostMapping("/g_delete")
    public String g_delete(@RequestParam("g_number") Integer g_number) {
        eventService.g_delete(g_number);
        return "redirect:/movie/community/gboard";
    }

    @GetMapping("/name")
    public @ResponseBody String an(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            return "세셔은 살아있다.";
        } else {
            return "세션살려";
        }
    }


}

