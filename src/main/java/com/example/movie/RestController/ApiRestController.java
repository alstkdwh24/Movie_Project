package com.example.movie.RestController;

import com.example.movie.LoginService.LoginService;
import com.example.movie.commandVO.LoginUpdateVO;
import com.example.movie.commandVO.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/movie/api")
public class ApiRestController {

    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;

    @GetMapping("/account")
    public ResponseEntity<ArrayList<LoginVO>> getAcount(@RequestParam("username") String username ){
        ArrayList<LoginVO> getAcount_List=loginService.getAcount(username);
        return new ResponseEntity<>(getAcount_List, HttpStatus.OK);
    }

    @PutMapping("/modify/{username}")
    public ResponseEntity<LoginVO> Mypage_modify(@PathVariable("username") String username, @RequestBody LoginUpdateVO update){
       loginService.get_Mypage_Modify(username,update);
        LoginVO vo = loginService.login(username);
        return new ResponseEntity<>(vo,HttpStatus.OK);


    }

}
