package com.example.movie.controller;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.ChatVO;
import com.example.movie.commandVO.EventVO;
import com.example.movie.util.Criteria;
import com.example.movie.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/movie/chats")
public class ChatController {
    @Autowired
    @Qualifier("chatService")
    public ChatService chatService;


    @GetMapping("/Question")
    public String Chat(HttpServletRequest request, Model model, Criteria cri, HttpSession session) {
        ArrayList<ChatVO> List = chatService.Question_show(cri);
        int total = chatService.Question_total(cri);
        PageVO PageVO = new PageVO(cri, total);
        model.addAttribute("list", List);
        model.addAttribute("pageVO", PageVO);
        System.out.println(PageVO);


        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);

        return "movie/chats/Question";
    }


    @GetMapping("/Question_writer")
    @PreAuthorize("hasAnyRole('ROLE_1')")
    public String writer(HttpSession session, Model model) {


        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        model.addAttribute("userSession", userDetails);
        return "movie/chats/Question_writer";
    }

    @GetMapping("/Question_detail")
    public String Question_detail(@RequestParam("Q_number") Integer Q_number, Model model, HttpSession session) {
        UserDetails userDetails = (UserDetails) session.getAttribute("user");


        if (userDetails == null) {
            return "redirect:/movie/login/login";
        } else {
            model.addAttribute("userSession", userDetails);
            ChatVO vo = chatService.Question_detail(Q_number);
            model.addAttribute("vo", vo);
            return "movie/chats/Question_detail";
        }
    }

    @PostMapping("/Question_writer")
    public String Q_writer(ChatVO vo, RedirectAttributes ra) {
        int result = chatService.Question_writer(vo);
        if (result == 1) {
            ra.addFlashAttribute("msg", "정상적으로 처리하였습니다.");
            System.out.println(vo.getQ_title());
        } else {
            ra.addFlashAttribute("msg", "음, 이건 아니에요.");
        }
        return "redirect:/movie/chats/Question";
    }


}



