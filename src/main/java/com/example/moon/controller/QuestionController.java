package com.example.moon.controller;

import com.example.moon.DTO.CommentCreateDTO;
import com.example.moon.DTO.CommentDTO;
import com.example.moon.DTO.QuestionDTO;
import com.example.moon.service.CommentService;
import com.example.moon.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        //根据文章id查询对应文章DTO对象
        QuestionDTO questionDTO = questionService.getById(id);
        //根据文章id查询对应文章的所有评论commentDTO
        List<CommentDTO> commentDTOList = commentService.listByQuestionId(id);

        //插入model
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOList", commentDTOList);
        //阅读数加一
        questionService.increaseView(id);
        return "question";
    }
}
