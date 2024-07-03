package com.example.moon.controller;

import com.example.moon.DTO.PageDTO;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.User;
import com.example.moon.service.QuestionService;
import jakarta.servlet.http.Cookie;
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
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable("action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

        User user=null;
        Cookie[] cookies = request.getCookies();//向网站请求获取cookie列表
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {//在cookie列表中找到token
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);//则去数据库中寻找是否有对应的cookie
                    request.getSession().setAttribute("user", user);//将查找结果赋给session
                    break;
                }
            }
        }
        if(user==null){
            return"redirect:/";
        }



        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","我的提问");
        }

        PageDTO pageDTO= questionService.list(user.getId(),page,size);
        model.addAttribute("pageDTO",pageDTO);
        return "profile";
    }
}
