package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "作者信息",description = "")
@Table(name="author")
public class Author{
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
    @ApiModelProperty(name = "作者名字",notes = "")
    private String name ;
    @ApiModelProperty(name = "作者简介",notes = "")
    private String information ;
    @ApiModelProperty(name = "作者链接",notes = "")
    private String url ;
    @ApiModelProperty(name = "作者合集",notes = "")
    private String title ;
    @ApiModelProperty(name = "作者标签",notes = "")
    private String tag ;
    @ApiModelProperty(name = "作者评分",notes = "")
    private Double score ;
}