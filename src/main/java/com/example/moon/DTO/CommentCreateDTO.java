package com.example.moon.DTO;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long commentator;
    private Long parentId;
    private String content;
    private Integer type;

}
