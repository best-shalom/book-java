package com.favor.book.dao;

import com.favor.book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据库user增删改查操作接口
 * 接口继承mybatis提供的BaseMapper方法，
 * @author Administrator
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // MyBatis-Plus会根据方法名自动生成SQL语句，因此在上述示例中，你不需要编写额外的XML配置文件。
    // User selectByAccount(@Param("account") String account);

}
