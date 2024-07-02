package com.example.moon.model;


//插入数据库的user类
public class User {
    private Integer id;
    private String name;
    private String account_id;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;

    public Long getGmt_Create() {
        return gmt_create;
    }

    public void setGmt_Create(Long gmt_create) {
        this.gmt_create = gmt_create;
    }

    public Long getGmt_Modified() {
        return gmt_modified;
    }

    public void setGmt_Modified(Long gmt_modified) {
        this.gmt_modified = gmt_modified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
