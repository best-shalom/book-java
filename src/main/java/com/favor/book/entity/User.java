package com.favor.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 1.Data注解为lombok提供的简化getter和setter的工具
 * 2.TableName注解为mybatis提供的，用于标识实体类对应数据库表
 */
@Data
@TableName(value = "user")
public class User {
    private Long id;
    private String account;
    private String password;
    private String name;
    private String information;
}
