package com.younger.community.dto;

import com.younger.community.model.User;
import lombok.Data;

/*
将user表与question表关联起来
根据question表的creator，拿到user表的id，进而拿到user表的avatarUrl图像信息
 */
@Data
public class QuestionDto {
    private int id;
    private String title;
    private String description;
    private String tags;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
