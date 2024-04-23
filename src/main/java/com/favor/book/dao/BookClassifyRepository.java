package com.favor.book.dao;

import com.favor.book.entity.BookClassify;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author CQ
 */
public interface BookClassifyRepository extends JpaRepository<BookClassify, Long> {
    BookClassify findByBookIdAndTagId(Long bookId, Long tagId);

    List<BookClassify> findAllByClassifyId(Long classifyId);
}
