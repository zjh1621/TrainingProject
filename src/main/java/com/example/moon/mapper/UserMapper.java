package com.example.moon.mapper;


import com.example.moon.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user (id,account_id,name,token,gmt_create,gmt_modified) values (#{id},#{account_id},#{name},#{token},#{gmt_create},#{gmt_modified})")
    void insert(User user);
}