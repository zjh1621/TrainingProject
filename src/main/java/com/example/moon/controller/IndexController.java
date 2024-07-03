package com.example.moon.controller;

import com.example.moon.DTO.PageDTO;
import com.example.moon.DTO.QuestionDTO;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.Question;
import com.example.moon.model.User;
import com.example.moon.service.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size) {
        Cookie[] cookies = request.getCookies();//向网站请求获取cookie列表
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {//在cookie列表中找到token
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);//则去数据库中寻找是否有对应的cookie
                    request.getSession().setAttribute("user", user);//将查找结果赋给session
                    break;
                }
            }
        }
        PageDTO pageDTO = questionService.list(page,size);//获取首页问题列表信息
        model.addAttribute("pageDTO", pageDTO);//将pageDTO注入model，传入前端
        return "index";
    }
}
