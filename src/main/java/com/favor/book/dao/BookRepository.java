package com.favor.book.dao;

import com.favor.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * 1.JPA和mybatis
 * 在企业开发过程中，除去一些特殊的要求外，基本上都会使用全自动或半自动的ORM框架代替原生JDBC进行数据库的访问。而在具体项目设计时，常常会根据项目业务情况进行技术选型。其中常用的ORM框架有：
 * Mybatis
 * Hibernate
 * Spring Data JPA
 * JdbcTemplate
 * 对比：<a href="https://www.cnblogs.com/threadj/p/13304870.html">...</a>
 * 2.dao层继承Repository<实体类型,主键id的类型>
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {
}
