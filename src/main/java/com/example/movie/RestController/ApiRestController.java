package com.example.movie.RestController;

import com.example.movie.LoginService.LoginService;
import com.example.movie.dto.LoginDTO;
import com.example.movie.dto.LoginUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/movie/api")
public class ApiRestController {

    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;

    @GetMapping("/account")
    public ResponseEntity<ArrayList<LoginDTO>> getAcount(@RequestParam("username") String username) {
        ArrayList<LoginDTO> getAcount_List = loginService.getAcount(username);
        return new ResponseEntity<>(getAcount_List, HttpStatus.OK);
    }

    @PutMapping("/modify/{username}")
    public ResponseEntity<LoginDTO> Mypage_modify(@PathVariable("username") String username, @RequestBody LoginUpdateDTO update) {
        loginService.get_Mypage_Modify(username, update);
        LoginDTO vo = loginService.login(username);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }
}
