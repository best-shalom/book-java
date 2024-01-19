package com.favor.book.service;

import com.favor.book.dao.BookRepository;
import com.favor.book.entity.Book;
import com.favor.book.utils.FileUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class BookService {
    @Resource
    FileUtil fileUtil;
    @Resource
    BookRepository bookRepository;
    @Resource
    JPAQueryFactory jpaQueryFactory;
    /**
     * 添加单本书籍
     * @param file 书籍文件
     * @return 上传结果
     */
    public String addOneBook(MultipartFile file){
        Map<String,String> res=fileUtil.addOneFile(file);
        if(res==null) {
            return "文件上传失败";
        }
        Book book=new Book();
        book.setName(res.get("name"));
        book.setCharacterName("1");
        book.setAuthorId(1L);
        book.setClassifyId(1L);
        book.setTypeId(1L);
        book.setTime(new Date());
        book.setInformation("1");
        int insert=bookRepository.insert(book);
        if(insert==0){
            return "书籍保存数据库失败";
        }
        return "书籍保存数据库成功";

    }

    /**
     * 按照小说类型or阅读类型筛选小说
     * @param classifyId 小说类型
     * @param typeId 阅读类型
     * @return 返回指定排序方式的小说列表
     */
    public List<Map<String,Object>> listBooks(Long classifyId,Long typeId){
        return null;
    }
}
