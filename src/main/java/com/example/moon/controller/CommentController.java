package com.example.moon.controller;

import com.example.moon.DTO.CommentDTO;
import com.example.moon.DTO.ResultDTO;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.model.Comment;
import com.example.moon.model.Question;
import com.example.moon.model.User;
import com.example.moon.service.CommentService;
import com.example.moon.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        //向数据库保存评论
        Comment comment = new Comment();
        comment.setCommentator(commentDTO.getCommentator());
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        commentService.insert(comment);

        Map<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("message", "成功");
        return objectObjectHashMap;
    }
}
