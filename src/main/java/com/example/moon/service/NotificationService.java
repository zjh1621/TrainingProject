package com.example.moon.service;

import com.example.moon.DTO.NotificationDTO;
import com.example.moon.DTO.PageDTO;
import com.example.moon.enums.NotificationEnum;
import com.example.moon.enums.NotificationStatusEnum;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.exception.CustomizeException;
import com.example.moon.mapper.CommentMapper;
import com.example.moon.mapper.NotificationMapper;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    /**
     * 分页功能
     * @param userId
     * @param page
     * @param size
     * @return
     */
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

        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds(offset,size));

        //toSet以去重
        Set<Long> notifierUserIdList = notificationList.stream().map(notify -> notify.getNotifier()).collect(Collectors.toSet());
        List<Long> userIdList = new ArrayList<>(notifierUserIdList);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                        .andIdIn(userIdList);
        List<User> userList = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));

        //将notification对象保存到DTO对象中
        List<NotificationDTO> notificationDTOList=new ArrayList<>();
        BeanUtils.copyProperties(notificationList,notificationDTOList);
        notificationDTOList=notificationList.stream().map(notification -> {
            NotificationDTO notificationDTO = getNotificationDTOWithoutNotifier(notification);
            notificationDTO.setNotifier(userMap.get(notification.getNotifier()));
            return notificationDTO;
        }).toList();

        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }

    /**
     * 获取未读消息数
     * @param userId
     * @return
     */
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andReadStatusEqualTo(NotificationStatusEnum.UNREAD.getType());
        return notificationMapper.countByExample(notificationExample);
    }

    //读取通知
    public NotificationDTO read(Long notificationId, User receiver) {

        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if (notification == null) {
            //通知不存在
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), receiver.getId())) {
            //访问别人的通知，无权限
            throw new CustomizeException(CustomizeErrorCode.UNAUTHORIZED_ACCESS);
        }
        if(notification.getReadStatus()==NotificationStatusEnum.UNREAD.getType()){
            //更新通知信息
            Notification updateNotification = new Notification();
            updateNotification.setReadStatus(NotificationStatusEnum.READ.getType());
            updateNotification.setId(notification.getId());
            notificationMapper.updateByPrimaryKeySelective(updateNotification);
        }

        //获取通知的DTO对象
        NotificationDTO notificationDTO = getNotificationDTOWithoutNotifier(notification);

        return notificationDTO;
    }

    /**
     * 将notification转化为notificationDTO，不包括notifier
     * @param notification
     * @return
     */
    private NotificationDTO getNotificationDTOWithoutNotifier(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        if(notification.getType()== NotificationEnum.REPLY_COMMENT.getType()){
            //如果回复的是评论，就找出这条评论，并将它回复的文章作为parentId，用于链接跳转
            Comment comment = commentMapper.selectByPrimaryKey(notification.getParentId());
            notification.setParentId(comment.getParentId());
            //设置outerMessage为评论内容
            notificationDTO.setOuterMessage(comment.getContent());
        }else if (notification.getType()== NotificationEnum.REPLY_QUESTION.getType()){
            //如果回复的是问题，就找出这个问题
            Question question = questionMapper.selectByPrimaryKey(notification.getParentId());
            //设置outerMessage为问题标题
            notificationDTO.setOuterMessage(question.getTitle());
        }
        return notificationDTO;
    }
}
