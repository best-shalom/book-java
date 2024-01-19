package com.favor.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 1.Data注解为lombok提供的简化getter和setter的工具
 * 2.TableName注解为mybatis提供的，用于标识实体类对应数据库表
 * 3.Table注解为QueryDSL提供，表示数据库表
 * 4.Entity:一般每有一个实体Bean配置了@Entity被检测到之后，就会在target的子目录中自动生成一个Q+实体名称 的类，这个类对我们使用QueryDSL非常重要，正是因为它，我们才使得QueryDSL能够构建类型安全的查询。
 * @author Administrator
 */
@ApiModel(value = "用户信息",description = "")
@Data
@Entity
@Table(name = "user")
public class User{
    /** 主键;自增主键 */
    @ApiModelProperty(name = "主键",notes = "自增主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    /** 创建时间;创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "创建时间")
    private Date createTime ;
    /** 更新时间;更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "更新时间")
    private Date updateTime ;
    /** 是否删除;是否删除 */
    @ApiModelProperty(name = "是否删除",notes = "是否删除")
    private int isDeleted ;
    /** 账号 */
    @ApiModelProperty(name = "账号",notes = "")
    private String account ;
    /** 密码 */
    @ApiModelProperty(name = "密码",notes = "")
    private String password ;
    /** 用户名 */
    @ApiModelProperty(name = "用户名",notes = "")
    private String name ;
    /** 简介 */
    @ApiModelProperty(name = "简介",notes = "")
    private String information ;
}
