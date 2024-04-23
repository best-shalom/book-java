package com.favor.book.dao;

import com.favor.book.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author CQ
 * @version 1.0
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
