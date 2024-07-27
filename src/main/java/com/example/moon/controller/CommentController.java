package com.example.moon.controller;

import com.example.moon.DTO.CommentCreateDTO;
import com.example.moon.DTO.CommentDTO;
import com.example.moon.DTO.ResultDTO;
import com.example.moon.enums.CommentTypeEnum;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.model.Comment;
import com.example.moon.model.User;
import com.example.moon.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        //向数据库保存评论
        Comment comment = new Comment();
        comment.setCommentator(commentCreateDTO.getCommentator());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        commentService.insert(comment);

        return ResultDTO.errorOf(CustomizeErrorCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/getComment/{id}", method = RequestMethod.GET)
    public ResultDTO getComments(@PathVariable(name = "id")Long id){
        List<CommentDTO> commentDTOList = commentService.listByParentId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(commentDTOList);
    }
}
