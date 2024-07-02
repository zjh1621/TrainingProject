package com.example.moon.controller;

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

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();//向网站请求获取cookie列表
        if(cookies!=null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {//在cookie列表中找到token
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);//则去数据库中寻找是否有对应的cookie
                    request.getSession().setAttribute("user", user);//将查找结果赋给session
                    break;
                }
            }
        }
        //获取首页问题列表信息
        List<QuestionDTO> questionDTOList = questionService.list();
        model.addAttribute("questionDTOList",questionDTOList);
        return "index";
    }
}
