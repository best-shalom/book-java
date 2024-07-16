package com.favor.book.entity;

import lombok.Data;

/**
 * @author CQ
 * @version 1.0
 * @date 2024/7/15 20:30
 * @description 用于ES检索的书籍长文本字段结构
 */

@Data
public class BookInfo {
    private String id;
    private String name;
    private String author;
    private String information;
}
