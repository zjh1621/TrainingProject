package com.example.moon.service;

import com.example.moon.DTO.QuestionDTO;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.Question;
import com.example.moon.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public List<QuestionDTO> list() {
        List<Question> questionList = questionMapper.list();//获取数据库中的question对象的list
        List<QuestionDTO> questionDTOList = new ArrayList<>();//传输层QuestionDTO对象的list
        for (Question question : questionList) {//遍历QuestionList
            User user = userMapper.findById(question.getCreator());//查找对应的user
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将Question对象保存到DTO对象中
            questionDTO.setUser(user);//为DTO元素加上id对应的User对象
            questionDTOList.add(questionDTO);//将DTO元素加入队列
        }
        return questionDTOList;//返回DTO队列
    }
}
