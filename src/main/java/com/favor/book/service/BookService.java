package com.favor.book.service;

import com.favor.book.dao.BookRepository;
import com.favor.book.entity.Book;
import com.favor.book.entity.QBook;
import com.favor.book.utils.FileUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
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
        bookRepository.save(book);
        return "书籍保存数据库成功";

    }

    /**
     * 按照小说类型or阅读类型筛选小说
     * @param classifyId 小说类型
     * @param typeId 阅读类型
     * @return 返回指定排序方式的小说列表
     */
    public Page<Book> listBooks(Pageable pageable, Long classifyId, Long typeId) {
        // Pageable 是Spring Data库中定义的一个接口，用于构造翻页查询，是所有分页相关信息的一个抽象，通过该接口，我们可以得到和分页相关所有信息（例如pageNumber、pageSize等），这样，Jpa就能够通过pageable参数来得到一个带分页信息的Sql语句。
        QBook book = QBook.book;
        // 初始化组装条件(book.isDeleted为0，即未删除的书籍)
        Predicate predicate = book.isDeleted.eq(0);
        // 执行动态条件拼装：如果参数==null，则predicate=predicate不变，否则就使用ExpressionUtils构建查询条件
        predicate = classifyId == null ? predicate : ExpressionUtils.and(predicate, book.classifyId.eq(classifyId));
        predicate = typeId == null ? predicate : ExpressionUtils.and(predicate, book.typeId.eq(typeId));
        // page的页面下标从0开始
        return bookRepository.findAll(predicate, pageable);
    }


}
