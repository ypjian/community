package com.younger.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtModified;
    private Long gmtCreate;
    private String avatarUrl;
}
