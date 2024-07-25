package com.example.moon.service;

import com.example.moon.enums.CommentTypeEnum;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.exception.CustomizeException;
import com.example.moon.mapper.CommentMapper;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.model.Comment;
import com.example.moon.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    QuestionService questionService;

    //向数据库保存评论
    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            //回复的评论或问题的ID非法
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            //回复的类型不正确
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            //todo
//            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
//            if (dbComment == null) {
//                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
//            }

        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            //被评论的文章的评论数加一
            if(comment.getType()==0){//type=0表示回复的是问题
                //被评论的文章的评论数加一
                questionService.increaseComment(comment.getParentId());
            }
        }
    }
}
