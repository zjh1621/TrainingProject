package com.example.moon.service;

import com.example.moon.DTO.PageDTO;
import com.example.moon.DTO.QuestionDTO;
import com.example.moon.exception.CustomizeErrorCode;
import com.example.moon.exception.CustomizeException;
import com.example.moon.mapper.QuestionMapper;
import com.example.moon.mapper.UserMapper;
import com.example.moon.model.Question;
import com.example.moon.model.QuestionExample;
import com.example.moon.model.User;
import org.apache.ibatis.session.RowBounds;
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

    public  PageDTO list(Integer userId, Integer page, Integer size) {
        {
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andCreatorEqualTo(userId);
            Integer totalCount = (int) questionMapper.countByExample(questionExample);//计算总条目数
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
        if(offset<0){
            offset=0;
        }
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);//计算总条目数

        // 获取数据库中的question对象的list
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();//传输层QuestionDTO对象的list

        PageDTO pageDTO = new PageDTO();
        for (Question question : questionList) {//遍历QuestionList
            User user = userMapper.selectByPrimaryKey(question.getCreator());//查找对应的user
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将Question对象保存到DTO对象中
            questionDTO.setUser(user);//为DTO元素加上id对应的User对象
            questionDTOList.add(questionDTO);//将DTO元素加入队列
        }
        pageDTO.setQuestionDTOList(questionDTOList);//向pageDTO注入questionDTOList
        pageDTO.setPagination(totalCount,page,size);//初始化pageDTO其他成员数据

        return pageDTO;//返回DTO队列
    }

    public PageDTO list(Integer page, Integer size) {
        {
            Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());//计算总条目数
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
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());//计算总条目数
        //获取数据库中的question对象的list
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();//传输层QuestionDTO对象的list

        PageDTO pageDTO = new PageDTO();
        for (Question question : questionList) {//遍历QuestionList
            User user = userMapper.selectByPrimaryKey(question.getCreator());//查找对应的user
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将Question对象保存到DTO对象中
            questionDTO.setUser(user);//为DTO元素加上id对应的User对象
            questionDTOList.add(questionDTO);//将DTO元素加入队列
        }
        pageDTO.setQuestionDTOList(questionDTOList);//向pageDTO注入questionDTOList
        pageDTO.setPagination(totalCount,page,size);//初始化pageDTO其他成员数据

        return pageDTO;//返回DTO队列
    }

    public QuestionDTO getById(Integer id) {
        //根据文章id查询对应文章
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){//文章不存在
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());//查找对应的user
        questionDTO.setUser(user);
        return questionDTO;
    }

    //文章发布与修改
    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //没有id，证明该文章刚被创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        }else{
            //有id，证明该文章正在被修改
            //设置要更新的内容
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            //设置更新规则：按文章ID匹配
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if(updated !=1){//文章不存在
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void increaseView(Integer id) {
        //获取现阅读量
        Integer oldViewCount = questionMapper.selectByPrimaryKey(id).getViewCount();
        //更新question
        Question updateQuestion = new Question();
        //设置新阅读量为现有阅读量+1
        updateQuestion.setViewCount(oldViewCount+1);
        QuestionExample updateQuestionExample = new QuestionExample();
        updateQuestionExample.createCriteria().andIdEqualTo(id);
        questionMapper.updateByExampleSelective(updateQuestion,updateQuestionExample);
    }
}
