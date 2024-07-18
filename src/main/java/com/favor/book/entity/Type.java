package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "阅读类型", description = "")
@Table(name="type")
public class Type{
    @Id
    @GeneratedValue
    @ApiModelProperty(name = "主键",notes = "自增主键")
    private Long id ;
    @ApiModelProperty(name = "创建时间",notes = "创建时间")
    private Date createTime ;
    @ApiModelProperty(name = "更新时间",notes = "更新时间")
    private Date updateTime ;
    @ApiModelProperty(name = "是否删除",notes = "是否删除")
    private int isDeleted ;
    @ApiModelProperty(name = "阅读类型",notes = "比如是否感兴趣、已经看完、值得回看、不好看等等")
    private String name ;
    @ApiModelProperty(name = "类型简介",notes = "")
    private String information ;
}