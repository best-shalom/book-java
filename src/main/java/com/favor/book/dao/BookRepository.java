package com.favor.book.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.favor.book.entity.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends BaseMapper<Book> {
}
