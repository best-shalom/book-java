package com.favor.book.service;

import com.favor.book.dao.BookClassifyRepository;
import com.favor.book.entity.BookClassify;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 需要在 Spring 配置类中将 BookClassifyService 标记为一个 Bean。
 * 注解：@Service 和 @Component 注解在 Spring 中的作用类似，它们都用于标记一个类为 Spring 的组件，并让 Spring 扫描和管理这个组件。它们之间的区别主要体现在语义上的差异：
 * 1.语义区别：
 * （1）@Service 注解表示该类是一个服务层组件，用于标记业务逻辑的实现类。它更加具体地描述了组件的作用。
 * （2）@Component 注解是一个通用的注解，可以用于任意类型的组件，包括服务层、持久层、控制层等。
 * 2.可读性：
 * （1）@Service 注解更加具有语义化，能够更清晰地表达组件的用途，使代码更易读和理解。
 * （2）@Component 注解相对来说更加通用，没有明确的语义，可能不够具有表达力。
 */
@Service
public class BookClassifyService {
    @Resource
    BookClassifyRepository bookClassifyRepository;

    public BookClassify getByBookIdAndClassifyId(Long bookId, Long classifyId) {
        return bookClassifyRepository.findByBookIdAndClassifyId(bookId, classifyId);
    }

    public BookClassify addBookClassify(Long bookId, Long classifyId) {
        BookClassify exist = getByBookIdAndClassifyId(bookId, classifyId);
        if (exist != null) {
            // 对应的书籍-标签关系已经存在，返回null表示存在
            return null;
        }
        BookClassify bookClassify = new BookClassify();
        bookClassify.setBookId(bookId);
        bookClassify.setClassifyId(classifyId);
        return bookClassifyRepository.save(bookClassify);
    }

    /**
     * 关联一本书对应的所有标签
     */
    public void addAllBookClassify(Long bookId, List<Long> classifyIds) {
        for (Long classifyId : classifyIds) {
            BookClassify res = addBookClassify(bookId, classifyId);
        }
    }
}
