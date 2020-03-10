package com.younger.community.mapper;

import com.younger.community.dto.QuestionDto;
import com.younger.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Indexed;

import java.util.List;

@Mapper
public interface QuestionMapper {

//    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tags) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags})")
//    public void create(Question question);
//
//    @Select("select * from question limit #{offset},#{size}")
//    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);
//
//    @Select("select count(1) from question")
//    Integer count();
//
//    //当用户点击我的问题时来数据库中查找问题信息
//    //调用list方法，传入各参数，然后@Param可以用于数据库查找
//    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
//    List<Question> listByUserId(@Param(value = "userId") Integer userId,@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);
//
//    @Select("select count(1) from question where creator = #{userId}")
//    Integer countByuserId(@Param(value = "userId")Integer userId);


    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tags) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question")
    Integer count();

    //当用户点击我的问题时来数据库中查找问题信息
    //调用list方法，传入各参数，然后@Param可以用于数据库查找
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId,@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByuserId(@Param(value = "userId")Integer userId);


    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") Integer id);

}




