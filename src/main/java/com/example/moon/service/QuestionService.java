package com.example.moon.service;

import com.example.moon.DTO.PageDTO;
import com.example.moon.DTO.QuestionDTO;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.Question;
import com.example.moon.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PageDTO list(Integer page, Integer size) {
        {
            Integer totalCount = questionMapper.count();//计算总条目数
            Integer totalPage = 0;
            //计算总页码
            if (totalCount % size == 0) {
                totalPage = totalCount / size;
            } else {
                totalPage = totalCount / size + 1;
            }
            //防止页码出错
            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }
        }            //防止页码出错
        //获取偏移量，（页码-1）乘每页数目，即是数据库查找字段的开始
        Integer offset = size * (page - 1);
        Integer totalCount = questionMapper.count();//计算总条目数
        List<Question> questionList = questionMapper.list(offset, size);//获取数据库中的question对象的list
        List<QuestionDTO> questionDTOList = new ArrayList<>();//传输层QuestionDTO对象的list

        PageDTO pageDTO = new PageDTO();
        for (Question question : questionList) {//遍历QuestionList
            User user = userMapper.findById(question.getCreator());//查找对应的user
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将Question对象保存到DTO对象中
            questionDTO.setUser(user);//为DTO元素加上id对应的User对象
            questionDTOList.add(questionDTO);//将DTO元素加入队列
        }
        pageDTO.setQuestionDTOList(questionDTOList);//向pageDTO注入questionDTOList
        pageDTO.setPagination(totalCount,page,size);//初始化pageDTO其他成员数据

        return pageDTO;//返回DTO队列
    }
}
