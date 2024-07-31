package com.example.moon.DTO;

import com.example.moon.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    //通知的id
    private Long id;

    //通知创建时间
    private Long gmtCreate;

    //0表示问题，1表示评论
    private int type;

    //通知所在的 文章 的id
    private Long parentId;

    //是否已读，0未读，1已读
    private Integer readStatus;



    //回复者的信息
    private User notifier;

    //文章标题或是评论略写
    private String outerMessage;
}
