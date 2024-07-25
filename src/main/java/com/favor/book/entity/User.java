package com.favor.book.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
// springboot3.0 jpa升级使用jakarta：jakarta.persistence升级到jakarta.persistence

import java.util.Date;


/**
 * 1.Data注解为lombok提供的简化getter和setter的工具
 * 2.TableName注解为mybatis提供的，用于标识实体类对应数据库表
 * 3.Table注解为QueryDSL提供，表示数据库表
 * 4.Entity:一般每有一个实体Bean配置了@Entity被检测到之后，就会在target的子目录中自动生成一个Q+实体名称 的类，这个类对我们使用QueryDSL非常重要，正是因为它，我们才使得QueryDSL能够构建类型安全的查询。
 * 5.GeneratedValue:标识为自增，相当于插入的时候插入的id == null，其中strategy指定了自增策略，参考：<a href="https://blog.csdn.net/weixin_45131680/article/details/131888114">...</a>
 * 6.DynamicInsert：需要mysql生成默认值的字段，sql语句中不写，而不是写null，写null的话mysql就会使用null而不是去生成默认值。参考：<a href="https://www.jianshu.com/p/12985c2b3993">...</a>
 * 默认生成字段解决方式：<a href="https://blog.csdn.net/harryshumxu/article/details/128716338">...</a>
 * 7.DynamicUpdate（默认为true）：如果在实体类上使用此注解，表示update对象的时候,生成动态的update语句,参考：<a href="https://blog.csdn.net/weixin_42126028/article/details/107548733">...</a>
 * 它的作用并不是更新指定字段，而是更新变化的字段;如果对象中某个属性为null，当执行更新操作时，如果这个字段在数据库中已经有值了当然会被null覆盖, 因为你save()的对象中这个字段跟数据库中的值不同。如果你更新的字段和数据库中的值相同则不更新该字段，解决方法是先把要更新的数据查询出来，查出原有值再把想变化的字段改变一下，再使用save()保存即可。
 * 使用后会貌似强制要求create_time的数据库字段类型为datetime而不是timestamp，不然会视为新的datetime类型字段添加到数据库中
 * @author Administrator
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@ApiModel(value = "用户信息", description = "")
@Table(name = "user")
public class User{
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
    @ApiModelProperty(name = "账号",notes = "")
    private String account ;
    @ApiModelProperty(name = "密码",notes = "")
    private String password ;
    @ApiModelProperty(name = "用户名",notes = "")
    private String name ;
    @ApiModelProperty(name = "简介",notes = "")
    private String information ;
}
