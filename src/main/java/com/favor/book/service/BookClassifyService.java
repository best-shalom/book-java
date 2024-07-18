package com.favor.book.service;

import com.favor.book.dao.BookClassifyRepository;
import com.favor.book.entity.Book;
import com.favor.book.entity.BookClassify;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Resource
    ClassifyService classifyService;
    // 1.避免循环依赖：
    // ┌─────┐
    // |  bookClassifyService
    // ↑     ↓
    // |  bookService
    // └─────┘
    // 通过添加@Lazy注解，您告诉Spring容器在需要使用bookService时才会进行实例化。这样，当调用getBookByClassifyName方法时，才会触发对bookService的实例化。
    // 请注意，在使用@Lazy注解后，确保在调用getBookByClassifyName方法之前已经完成了对bookService bean的初始化。否则，将会抛出NullPointerException异常。
    // 2.Lazy会产生的问题：
    // 查询结果包含代理对象的主要原因是Hibernate的延迟加载机制。在Hibernate中，当您查询实体对象时，如果该对象有延迟加载的关联属性（通常是使用FetchType.LAZY），Hibernate会将这些关联属性设置为代理对象，而不会立即加载数据。
    // 这种延迟加载的机制可以提高性能，避免不必要的数据加载，但也可能导致序列化问题，特别是在将实体对象转换为JSON格式时。因为默认情况下Jackson库无法正确处理这些Hibernate代理对象，从而导致序列化异常。
    // 通常情况下，您可以通过以下方式避免查询结果包含代理对象：
    // （1）使用JOIN FETCH或者其他方式预先加载关联属性，确保在查询时就加载所有需要的数据，而不是延迟加载。
    // （2）在需要返回给前端的实体类中使用Jackson注解@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})来排除Hibernate代理对象中的属性，从而避免序列化异常。
    // 如果您的其他查询没有产生这样的问题，可能是因为这些查询没有延迟加载的关联属性，或者已经在查询时进行了数据加载，所以没有涉及到Hibernate代理对象的序列化问题。
    @Lazy
    @Resource
    BookService bookService;

    public BookClassify getByBookIdAndTagId(Long bookId, Long tagId) {
        return bookClassifyRepository.findByBookIdAndTagId(bookId, tagId);
    }

    public BookClassify addBookClassify(Long bookId, Long classifyId, Long tagId) {
        BookClassify exist = getByBookIdAndTagId(bookId, tagId);
        if (exist != null) {
            // 对应的书籍-标签关系已经存在，返回null表示存在
            return null;
        }
        BookClassify bookClassify = new BookClassify();
        bookClassify.setBookId(bookId);
        bookClassify.setClassifyId(classifyId);
        bookClassify.setTagId(tagId);
        return bookClassifyRepository.save(bookClassify);
    }

    /**
     * 关联一本书对应的所有标签
     */
    public void addAllBookClassify(Long bookId, Long classifyId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            BookClassify res = addBookClassify(bookId, classifyId, tagId);
        }
    }

    /**
     * 根据classifyId，从关联表中获取对应的bookId
     *
     * @return 返回关联的bookId
     */
    public List<Long> getConnectionByClassify(Long classifyId) {
        List<BookClassify> bookClassifyList = bookClassifyRepository.findAllByClassifyId(classifyId);
        return bookClassifyList.stream().map(BookClassify::getBookId).collect(Collectors.toList());
    }

    /**
     * 根据classify列表，获取符合条件的书籍(同时满足)，并返回书籍列表
     *
     * @param classifyNameList 作为筛选条件的标签List
     * @return 返回对应的Book列表
     */
    public List<Book> getBookByClassifyName(List<String> classifyNameList) {
        List<Long> classifyIdList = classifyService.getClassifyIdsByNames(classifyNameList);
        Set<Long> bookIdSetRes = new HashSet<>();
        // 使用第一个classifyId初始化set，用于后续求交集
        if (!classifyIdList.isEmpty()) {
            bookIdSetRes.addAll(getConnectionByClassify(classifyIdList.get(0)));
        }
        for (int i = 1; i < classifyIdList.size(); i++) {
            Set<Long> bookIdSet = new HashSet<>(getConnectionByClassify(classifyIdList.get(i)));
            // 添加时求交集
            bookIdSetRes.retainAll(bookIdSet);
        }
        System.out.println(bookService);

        return bookService.getBookByIdList(new ArrayList<>(bookIdSetRes));
    }
}
