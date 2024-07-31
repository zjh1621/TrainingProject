package com.example.moon.service;

import com.example.moon.DTO.CommentDTO;
import com.example.moon.enums.CommentTypeEnum;
import com.example.moon.enums.NotificationEnum;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.exception.CustomizeException;
import com.example.moon.mapper.CommentMapper;
import com.example.moon.mapper.NotificationMapper;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

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
            //获取回复的评论的对象
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //保存评论
            commentMapper.insertSelective(comment);
            //该评论回复的评论的评论数加一
            this.increaseComment(dbComment.getId());
            //该评论对应的文章的评论数加一
            questionService.increaseComment(dbComment.getParentId());
            //向数据库保存该评论对应的通知对象
            int type=NotificationEnum.REPLY_COMMENT.getType();
            Long notifier = comment.getCommentator();
            Long parentId = comment.getParentId();
            Long receiverId = dbComment.getCommentator();
            createNotification(type, notifier, parentId, receiverId);

        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //保存评论
            commentMapper.insertSelective(comment);
            //被评论的文章的评论数加一
            questionService.increaseComment(comment.getParentId());
            //向数据库保存该评论对应的通知对象
            int type=NotificationEnum.REPLY_QUESTION.getType();
            Long notifier = comment.getCommentator();
            Long parentId = comment.getParentId();
            Long receiverId = questionMapper.selectByPrimaryKey(comment.getParentId()).getCreator();
            createNotification(type, notifier, parentId, receiverId);
        }
    }

    /**
     * 向数据库保存该评论对应的通知对象
     * @param type
     * @param notifierId
     * @param parentId
     * @param receiverId
     */
    private void createNotification(int type,Long notifierId,Long parentId,Long receiverId) {
        if(notifierId == receiverId){
            return;
        }
        Notification notification = new Notification();
        //当前时间
        notification.setGmtCreate(System.currentTimeMillis());
        //通知类型：回复的是问题还是评论
        notification.setType(type);
        //通知的发起人
        notification.setNotifier(notifierId);
        //通知对应的问题或评论ID
        notification.setParentId(parentId);
        //通知的接收人
        notification.setReceiver(receiverId);
        notificationMapper.insertSelective(notification);
    }

    //增加评论数
    private void increaseComment(Long id) {
        //获取现评论量
        Integer oldCommentCount = commentMapper.selectByPrimaryKey(id).getCommentCount();
        //更新comment
        Comment updateComment = new Comment();
        //设置新阅读量为现有阅读量+1
        updateComment.setCommentCount(oldCommentCount+1);
        CommentExample updateCommentExample = new CommentExample();
        updateCommentExample.createCriteria().andIdEqualTo(id);
        commentMapper.updateByExampleSelective(updateComment,updateCommentExample);
    }

    //列举出问题的回复列表
    public List<CommentDTO> listByParentId(Long questionId, int type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(type);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取所有评论的用户id列表，并去重
        Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //根据用户id列表获取用户对象
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //建立用户id与用户对象的map表
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        //将comment转化为commentDTO，在此过程中向commentDTO加入user对象
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).toList();


        return commentDTOS;
    }
}
