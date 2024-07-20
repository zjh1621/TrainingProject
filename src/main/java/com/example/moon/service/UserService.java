package com.example.moon.service;

import com.example.moon.mapper.UserMapper;
import com.example.moon.model.User;
import com.example.moon.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> dbUser = userMapper.selectByExample(userExample);
        if(dbUser.size()==0){
            //用户不存在，则新建用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //用户存在，则更新内容
            User updateUser=new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setToken(user.getToken());
            updateUser.setName(user.getName());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.get(0).getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
