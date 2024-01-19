package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator
 */
@ApiModel(value = "阅读类型",description = "")
@Data
@Entity
@Table(name="type")
public class Type{
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
    /** 阅读类型;比如是否感兴趣、已经看完、值得回看、不好看等等 */
    @ApiModelProperty(name = "阅读类型",notes = "比如是否感兴趣、已经看完、值得回看、不好看等等")
    private String name ;
    /** 类型简介 */
    @ApiModelProperty(name = "类型简介",notes = "")
    private String information ;
}
