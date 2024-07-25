package com.example.movie.controller;

import com.example.movie.ChatService.ChatService;
import com.example.movie.commandVO.ChatVO;
import com.example.movie.util.Criteria;
import com.example.movie.util.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("movie/chats")
public class ChatController {
    @Autowired
    @Qualifier("chatService")
    public ChatService chatService;


    @GetMapping("/Question")
    public String Chat(HttpServletRequest request, Model model, Criteria cri) {
        ArrayList<ChatVO>List=chatService.Question_show(cri);
        int total= chatService.Question_total(cri);
        PageVO pageVO=new PageVO(cri, total);
        model.addAttribute("list",List);
        model.addAttribute("pageVO",pageVO);
        System.out.println(pageVO);


        return "movie/chats/Question";
    }




    @GetMapping("/Question_writer")
    public String writer(){
        return "movie/chats/Question_writer";
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



