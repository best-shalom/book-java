package com.favor.book.dao;

import com.favor.book.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
