package com.pictureproject.controller;

import com.pictureproject.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final HttpSession httpSession;

    @GetMapping(value = "/")
    public String main(Model model){
        SessionUser user=(SessionUser) httpSession.getAttribute("user");
        if(user!=null){
            model.addAttribute("userName",user.getName());
        }else{
            model.addAttribute("userName","로그인");
        }
        return "main";
    }
}
