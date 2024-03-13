package com.favor.book.dao;

import com.favor.book.entity.Classify;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface ClassifyRepository extends JpaRepository<Classify, Long> {
    /**
     * 在 Spring Data JPA 中，按照一定的命名规范编写 Repository 方法可以实现自动化的查询功能。通过命名方法 findByName 就可以指定按照 name 字段进行查询。
     * <p>
     * 具体解释如下：
     * find: 表明这是一个查询方法。
     * By: 表示接下来是根据哪个字段进行查询。
     * Name: 表示具体要查询的字段名，这里就是按照 name 字段进行查询。
     */
    Classify findByName(String name);
}
