package com.example.moon.DTO;

import com.example.moon.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private int likeCount;
    private int commentCount;
    private String content;
    private User user;
}
