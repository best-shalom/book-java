package com.favor.book.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author CQ
 * @version 1.0
 * @date 2024/7/15 20:30
 * @description 用于ES检索的书籍长文本字段结构
 */

@Document(indexName = "book")
public class BookDocument {
    @Id
    private String id;
    private String name;
    private String author;
    private String information;
}
