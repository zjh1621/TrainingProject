package com.example.moon.controller;

import com.example.moon.DTO.PageDTO;
import com.example.moon.mapper.UserMapper;
import com.example.moon.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size) {

        PageDTO pageDTO = questionService.list(page,size);//获取首页问题列表信息
        model.addAttribute("pageDTO", pageDTO);//将pageDTO注入model，传入前端
        return "index";
    }
}
