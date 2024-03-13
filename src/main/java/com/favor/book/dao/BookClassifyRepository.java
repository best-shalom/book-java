package com.favor.book.dao;

import com.favor.book.entity.BookClassify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClassifyRepository extends JpaRepository<BookClassify, Long> {
    BookClassify findByBookIdAndClassifyId(Long bookId, Long classifyId);
}
