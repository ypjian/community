package com.younger.community.service;

import com.younger.community.dto.PageDto;
import com.younger.community.dto.QuestionDto;
import com.younger.community.mapper.QuestionMapper;
import com.younger.community.mapper.UserMapper;
import com.younger.community.model.Question;
import com.younger.community.model.QuestionExample;
import com.younger.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    26 添加分页功能
     */
    public PageDto list(Integer page, Integer size) {

        //创建questiondto
        PageDto pageDto = new PageDto();
        Integer totalPage;
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());

        if(totalCount % size == 0) {
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page < 1) {
            page = 1;
        }
        if(page > totalPage) {
            page = totalPage;
        }

        pageDto.setpage(totalPage,page);

        //size*(page-1)
        Integer offset = size * (page - 1);

        //1.使用questiomapper查询到信息封装成列表
//        List<Question> questions = questionMapper.list(offset,size);

        //使用MBG生成分页
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));


        List<QuestionDto> questionDtoList = new ArrayList<>();



        for (Question question : questions) {
            //根据question的creator找到对应的userid
            User user = userMapper.selectByPrimaryKey(question.getCreator());

            //questionDto.setId(question.getId());
            //快速的将question的所有属性拷贝到questionDto
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        //将questionDtoList封装到
        pageDto.setQuestions(questionDtoList);
        return pageDto;
    }

    public PageDto list(Integer userId, Integer page, Integer size) {
        //创建questiondto
        PageDto pageDto = new PageDto();
        Integer totalPage;
//        Integer totalCount = questionMapper.countByuserId(userId);

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if(totalCount % size == 0) {
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page < 1) {
            page = 1;
        }
        if(page > totalPage) {
            page = totalPage;
        }

        pageDto.setpage(totalPage,page);

        //size*(page-1)
        Integer offset = size * (page - 1);

        //根据useid去数据库查找该用户的专属信息

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDto> questionDtoList = new ArrayList<>();



        for (Question question : questions) {
            //根据question的creator找到对应的userid
            User user = userMapper.selectByPrimaryKey(question.getCreator());

            //questionDto.setId(question.getId());
            //快速的将question的所有属性拷贝到questionDto
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        //将questionDtoList封装到
        pageDto.setQuestions(questionDtoList);
        return pageDto;
    }

    public QuestionDto getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void creatOrUpdate(Question question) {
        if(question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        }else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTags(question.getTags());
            QuestionExample example= new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion,example);
        }
    }
}
