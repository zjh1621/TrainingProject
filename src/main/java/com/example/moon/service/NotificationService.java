package com.example.moon.service;

import com.example.moon.DTO.CommentDTO;
import com.example.moon.DTO.NotificationDTO;
import com.example.moon.DTO.PageDTO;
import com.example.moon.DTO.QuestionDTO;
import com.example.moon.enums.NotificationEnum;
import com.example.moon.mapper.CommentMapper;
import com.example.moon.mapper.NotificationMapper;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PageDTO list(Long userId, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        pageDTO.setPagination(totalPage,page,size);

        Integer offset= size*(page-1);
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds(offset,size));

        //toSet以去重
        Set<Long> notifierUserIdList = notificationList.stream().map(notify -> notify.getNotifier()).collect(Collectors.toSet());
        List<Long> userIdList = new ArrayList<>(notifierUserIdList);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                        .andIdIn(userIdList);
        List<User> userList = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));

        List<NotificationDTO> notificationDTOList=new ArrayList<>();
        BeanUtils.copyProperties(notificationList,notificationDTOList);//将notification对象保存到DTO对象中
        notificationDTOList=notificationList.stream().map(notification -> {
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            if(notification.getType()== NotificationEnum.REPLY_COMMENT.getType()){
                //如果回复的是评论，就找出这条评论，并将它回复的文章作为parentId
                Comment comment = commentMapper.selectByPrimaryKey(notification.getParentId());
                notification.setParentId(comment.getParentId());
            }
            notificationDTO.setNotifier(userMap.get(notification.getNotifier()));
            return notificationDTO;
        }).toList();
        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }
}
