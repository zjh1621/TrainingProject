package com.example.moon.model;


import lombok.Data;

//插入数据库的user类
@Data
public class User {
    private Integer id;
    private String name;
    private String account_id;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;
}
