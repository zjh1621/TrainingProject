package com.example.moon.controller;

import com.example.moon.DTO.PageDTO;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.User;
import com.example.moon.service.NotificationService;
import com.example.moon.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable("action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "8") Integer size){

        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return"redirect:/";
        }
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            //分页
            PageDTO pageDTO= questionService.list(user.getId(),page,size);
            model.addAttribute("pageDTO",pageDTO);


        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            model.addAttribute("unreadCount","");
            //分页
            PageDTO pageDTO= notificationService.list(user.getId(),page,size);
            model.addAttribute("pageDTO",pageDTO);
        }

        return "profile";
    }
}
