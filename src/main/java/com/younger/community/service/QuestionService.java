package com.younger.community.service;

import com.younger.community.dto.QuestionDto;
import com.younger.community.mapper.QuestionMapper;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.Question;
import com.younger.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
同时使用questionmapper和usermapper，service用于组装
 */

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    /*
    将所有的发布信息封装到questionDtoList中，包括question的信息和user的id及avatarUrl图像信息
     */
    public List<QuestionDto> list() {
        //1.使用questiomapper查询到信息封装成列表
        List<Question> questions = questionMapper.list();
        List<QuestionDto> questionDtoList = new ArrayList<>();
        for (Question question : questions) {
            //根据question的creator找到对应的userid
            User user = userMapper.findById(question.getCreator());
            //创建questiondto
            QuestionDto questionDto = new QuestionDto();
            //questionDto.setId(question.getId());
            //快速的将question的所有属性拷贝到questionDto
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        return questionDtoList;
    }
}
