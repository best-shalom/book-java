package com.favor.book.dao;

import com.favor.book.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Administrator
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);
}
