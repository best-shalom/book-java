package com.favor.book.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ApiModel注解是用在接口相关的实体类上的注解，它主要是用来对使用该注解的接口相关的实体类添加额外的描述信息，常常和@ApiModelProperty注解配合使用
 * @author Administrator
 */
@ApiModel(value = "分组类型",description = "")
@Data
@Entity
@Table(name="classify")
public class Classify{
    /** 主键;自增主键 */
    @ApiModelProperty(name = "主键",notes = "自增主键")
    @Id
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
    /** 分组名 */
    @ApiModelProperty(name = "分组名",notes = "")
    private String name ;
    /** 分组简介 */
    @ApiModelProperty(name = "分组简介",notes = "")
    private String information ;
}