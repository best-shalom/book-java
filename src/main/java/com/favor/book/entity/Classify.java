package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

/**
 * ApiModel注解是用在接口相关的实体类上的注解，它主要是用来对使用该注解的接口相关的实体类添加额外的描述信息，常常和@ApiModelProperty注解配合使用
 * @author Administrator
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "书籍分类", description = "")
@Table(name="classify")
public class Classify{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "主键",notes = "自增主键")
    private Long id ;
    @ApiModelProperty(name = "创建时间",notes = "创建时间")
    private Date createTime ;
    @ApiModelProperty(name = "更新时间",notes = "更新时间")
    private Date updateTime ;
    @ApiModelProperty(name = "是否删除",notes = "是否删除")
    private int isDeleted ;
    @ApiModelProperty(name = "分类名", notes = "")
    private String name ;
    @ApiModelProperty(name = "分类简介", notes = "")
    private String information ;
    @ApiModelProperty(name = "分类链接", notes = "")
    private String url;
}