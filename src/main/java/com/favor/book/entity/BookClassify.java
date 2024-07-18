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
@ApiModel(value = "书籍分组关联", description = "")
@Table(name = "book_classify")
public class BookClassify {
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
    @ApiModelProperty(name = "书籍id", notes = "")
    private Long bookId;
    @ApiModelProperty(name = "关联分组id", notes = "")
    private Long classifyId;
    @ApiModelProperty(name = "关联标签id", notes = "")
    private Long tagId;
}