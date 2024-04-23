package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author CQ
 * @version 1.0
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "书籍标签", description = "")
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue
    @ApiModelProperty(name = "主键", notes = "自增主键")
    private Long id;
    @ApiModelProperty(name = "创建时间", notes = "创建时间")
    private Date createTime;
    @ApiModelProperty(name = "更新时间", notes = "更新时间")
    private Date updateTime;
    @ApiModelProperty(name = "是否删除", notes = "是否删除")
    private int isDeleted;
    @ApiModelProperty(name = "标签名", notes = "")
    private String name;
    @ApiModelProperty(name = "标签简介", notes = "")
    private String information;
    @ApiModelProperty(name = "标签链接", notes = "")
    private String url;
}
