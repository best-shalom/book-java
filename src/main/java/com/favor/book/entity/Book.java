package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Administrator
 */
@ApiModel(value = "书籍信息",description = "")
@Data
@Entity
@Table(name="book")
public class Book{
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
    /** 书名 */
    @ApiModelProperty(name = "书名",notes = "")
    private String name ;
    /** 小说主角 */
    @ApiModelProperty(name = "小说主角",notes = "")
    private String characterName ;
    /** 小说作者 */
    @ApiModelProperty(name = "小说作者",notes = "")
    private Long authorId ;
    /** 小说类型 */
    @ApiModelProperty(name = "小说类型",notes = "")
    private Long classifyId ;
    /** 阅读类型;比如是否感兴趣、已经看完、值得回看、不好看等等 */
    @ApiModelProperty(name = "阅读类型",notes = "比如是否感兴趣、已经看完、值得回看、不好看等等")
    private Long typeId ;
    /** 完结时间 */
    @ApiModelProperty(name = "完结时间",notes = "")
    private Date time ;
    /** 小说简介 */
    @ApiModelProperty(name = "小说简介",notes = "")
    private String information ;
    /** 内容标签;分类之外的一些具体标签 */
    @ApiModelProperty(name = "内容标签",notes = "分类之外的一些具体标签")
    private String tag ;
    /** 我的评价 */
    @ApiModelProperty(name = "我的评价",notes = "")
    private String evaluate ;
}
