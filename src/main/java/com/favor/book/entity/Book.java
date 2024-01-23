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

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "书籍信息", description = "")
@Table(name="book")
public class Book{
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
    @ApiModelProperty(name = "书名",notes = "")
    private String newName;
    @ApiModelProperty(name = "文件名", notes = "")
    private String oldName;
    @ApiModelProperty(name = "小说主角",notes = "")
    private String characterName ;
    @ApiModelProperty(name = "小说作者",notes = "")
    private Long authorId ;
    @ApiModelProperty(name = "小说类型",notes = "")
    private Long classifyId ;
    @ApiModelProperty(name = "阅读类型",notes = "比如是否感兴趣、已经看完、值得回看、不好看等等")
    private Long typeId ;
    @ApiModelProperty(name = "完结时间",notes = "")
    private Date finishTime;
    @ApiModelProperty(name = "上传时间", notes = "")
    private Date uploadTime;
    @ApiModelProperty(name = "上传用户", notes = "")
    private Long uploadUserId;
    @ApiModelProperty(name = "小说简介",notes = "")
    private String information ;
    @ApiModelProperty(name = "小说大小", notes = "")
    private String fileSize;
    @ApiModelProperty(name = "文件路径", notes = "")
    private String filePath;
    @ApiModelProperty(name = "内容标签",notes = "分类之外的一些具体标签")
    private String tag ;
    @ApiModelProperty(name = "我的评价",notes = "")
    private String evaluate ;
}