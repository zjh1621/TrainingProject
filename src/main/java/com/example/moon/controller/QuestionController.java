package com.example.moon.controller;

import com.example.moon.DTO.QuestionDTO;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.model.Question;
import com.example.moon.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        //根据文章id查询对应文章DTO对象
        QuestionDTO questionDTO = questionService.getById(id);
        //插入model
        model.addAttribute("questionDTO",questionDTO);

        return "question";
    }
}
