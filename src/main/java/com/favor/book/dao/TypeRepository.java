package com.favor.book.dao;

import com.favor.book.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Administrator
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
    // 如果name存在重复，则返回第一个（使用findByName的话就会返回全部符合的）
    Optional<Type> findFirstByName(String name);
}
