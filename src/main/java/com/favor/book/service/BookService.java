package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.BookRepository;
import com.favor.book.entity.*;
import com.favor.book.utils.FileUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 */
@Service
public class BookService {
    @Resource
    FileUtil fileUtil;
    @Resource
    BookRepository bookRepository;

    // TODO spring的装配和注入？
    @Resource
    JPAQueryFactory jpaQueryFactory;

    @Resource
    AuthorService authorService;
    @Resource
    ClassifyService classifyService;
    @Resource
    BookClassifyService bookClassifyService;
    @Resource
    TagService tagService;
    @Resource
    TypeService typeService;

    static final String BASE_URL = "https://www.ttxxll.com";

    /**
     * 添加单本书籍
     *
     * @param file 书籍文件
     * @return 上传结果
     */
    public Result addOneBookFile(MultipartFile file, String newName) {
        Map<String, String> res = fileUtil.addOneFile(file, newName);
        if (res == null) {
            return Result.error("文件上传失败");
        }
        String name = res.get("newName");
        String oldName = res.get("oldName");
        String size = res.get("size");
        String filePath = res.get("filePath");
        Book book = new Book();
        book.setNewName(name);
        book.setOldName(oldName);
        // 主角，从文件读取
        book.setCharacterName("1");
        // id系列，从文件读取后在相应的表查询
        book.setAuthorId(1L);
        book.setTypeId(1L);
        // 完结时间，从文件读取
        book.setFinishTime(new Date());
        book.setUploadTime(new Date());
        // 上传用户，从登录信息取
        book.setUploadUserId(1L);
        // 小说简介，从文件读取
        book.setInformation("1");
        book.setFileSize(size);
        book.setFilePath(filePath);
        // 小说标签，从文件取
        book.setTag("1");
        // 评价，后续阅读新增
        bookRepository.save(book);
        return Result.success("书籍保存数据库成功");

    }

    public Result downloadOneBookFile(Long id, String savePath) {
        Book book = getBookById(id);
        String filePath = book.getFilePath();
        String fileName = book.getNewName();
        if (fileUtil.downloadOneFile(filePath, fileName, savePath)) {
            return Result.success("下载成功");
        }
        return Result.error("下载失败");

    }


    public Book getBookById(Long id) {
        return bookRepository.getById(id);
    }

    public List<Book> getBookByIdList(List<Long> bookIdList) {
        List<Book> bookList = new ArrayList<>();
        for (Long bookId : bookIdList) {
            bookList.add(getBookById(bookId));
        }
        return bookList;
    }
    public Book getBookByName(String name) {
        return bookRepository.findByNewName(name);
    }

    public Book addBook(Book book) {
        Book exist = getBookByName(book.getNewName());
        if (exist != null) {
            return exist;
        }
        return bookRepository.save(book);
    }

    /**
     * 根据选择的参数更新书籍信息
     *
     * @param bookId       id标识书籍
     * @param typeName     添加、更新、删除阅读类型
     * @param classifyName 添加、更新、删除小说分类
     * @param evaluate     添加、更新、删除评价
     */
    public Result updateBookInfo(Long bookId, String typeName, String classifyName, String evaluate) {
        Book book = getBookById(bookId);
        if (book == null) {
            return Result.error("书籍不存在");
        }
        if (typeName != null) {
            Long typeId = typeService.getTypeIdByName(typeName);
            if (typeId == null) {
                return Result.error("阅读类型不存在");
            }
            book.setTypeId(typeId);
        }
        if (classifyName != null) {
            Long classifyId = classifyService.getClassifyIdByName(classifyName);
            if (classifyId == null) {
                return Result.error("小说分类不存在");
            }
            book.setClassifyId(classifyId);
        }
        if (evaluate != null) {
            book.setEvaluate(evaluate);
        }
        return Result.success(bookRepository.save(book));
    }

    /**
     * 按照小说类型or阅读类型筛选小说
     *
     * @param classifyName    小说分类
     * @param tagName         小说标签
     * @param typeName        显示某个阅读类型下的所有书籍
     * @param orderByUpload 是否根据上传时间排序，0-否，1-正序，2-倒序
     * @param orderByFinish 是否根据完结时间排序，0-否，1-正序，2-倒序
     * @return 返回指定排序方式的小说列表
     */
    public Result listBooks(Pageable pageable, String classifyName, String tagName, String typeName, int orderByUpload, int orderByFinish) {
        List<Long> classifyIdList = classifyName != null ? Stream.of(classifyName.split(","))
                // 检查 classifyName 是否不为 null。如果 classifyName 为 null，则 classifyIdList 也为 null。
                // 将逗号分隔的分类名称字符串转换为一个流（Stream），每个分类名称作为流中的一个元素。
                .map(String::trim)
                // 对流中的每个分类名称调用 trim 方法，去除名称两端的空格。
                .filter(name -> !name.isEmpty())
                // 过滤掉空字符串（即长度为 0 的字符串）,即filter只保留不为空的
                .map(classifyService::getClassifyIdByName)
                // 对流中的每个分类名称调用 classifyService.getClassifyIdByName 方法，将其转换为分类 ID。
                .filter(Objects::nonNull)
                // 过滤掉 null 值，确保分类 ID 列表中没有无效的 ID。
                .collect(Collectors.toList()) : null;
        // 将处理后的流收集为一个列表（List）。
        List<String> tagNameList = tagName != null ? Stream.of(tagName.split(",")).map(String::trim).filter(name -> !name.isEmpty()).collect(Collectors.toList()) : null;
        List<Long> typeIdList = typeName != null ? Stream.of(typeName.split(",")).map(String::trim).filter(name -> !name.isEmpty()).map(typeService::getTypeIdByName).filter(Objects::nonNull).collect(Collectors.toList()) : null;

        // Pageable 是Spring Data库中定义的一个接口，用于构造翻页查询，是所有分页相关信息的一个抽象，通过该接口，我们可以得到和分页相关所有信息（例如pageNumber、pageSize等），这样，Jpa就能够通过pageable参数来得到一个带分页信息的Sql语句。
        // QBook 是一个由 QueryDSL 生成的类，它代表数据库表中的 Book 实体。通过 QBook.book 获取这个类的实例，可以方便地构建查询条件。
        QBook book = QBook.book;
        // BooleanExpression 是 QueryDSL 中用于表示布尔条件的类。初始化组装条件(book.isDeleted为0，即未删除的书籍)
        BooleanExpression predicate = book.isDeleted.eq(0);
        // 执行动态条件拼装
        if (classifyIdList != null && !classifyIdList.isEmpty()) {
            // 构建一个条件，筛选分类 ID 在 classifyIdList 中的书籍。predicate.and(...) 将这个条件与之前的条件组合，要求同时满足
            predicate = predicate.and(book.classifyId.in(classifyIdList));
        }
        if (tagNameList != null && !tagNameList.isEmpty()) {
            // 使用 tagNameList.stream() 将标签名称列表转换为流（stream）。
            // map(book.tag::contains) 将每个标签名称映射为一个条件，检查书籍的标签字段是否包含该标签名称。
            // .reduce(BooleanExpression::and) 将这些条件使用逻辑 and 组合成一个条件，即标签字段必须包含所有标签名称。
            // orElse(Expressions.FALSE.isFalse()) 如果没有标签名称，则返回一个永远为 false 的条件。
            // predicate = predicate.and(tagExpression) 将这个条件与之前的条件组合，要求同时满足。
            BooleanExpression tagExpression = tagNameList.stream().map(book.tag::contains).reduce(BooleanExpression::and).orElse(Expressions.FALSE.isFalse());
            predicate = predicate.and(tagExpression);
        }
        if (typeIdList != null && !typeIdList.isEmpty()) {
            predicate = predicate.and(book.typeId.in(typeIdList));
        }
        // page的页面下标从0开始
        // jpaQueryFactory 是 QueryDSL 的 JPAQueryFactory 实例，用于创建和执行查询。
        // selectFrom(book) 指定要从 Book 表中查询数据。
        List<Book> allBooks = jpaQueryFactory.selectFrom(book)
                .where(predicate)
                .orderBy(book.createTime.asc())
                .fetch();
        // 计算总记录数
        long totalCount = allBooks.size();
        // 计算总页数
        long totalPages = (totalCount + pageable.getPageSize() - 1) / pageable.getPageSize();
        // 分页
        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min((int) (pageable.getOffset() + pageable.getPageSize()), allBooks.size());
        List<Book> bookList = allBooks.subList(fromIndex, toIndex);
        // 构建page信息
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageNumber", pageable.getPageNumber());
        pageInfo.put("pageSize", pageable.getPageSize());
        pageInfo.put("totalPages", totalPages);
        pageInfo.put("totalCount", totalCount);

        Map<String, Object> res = new HashMap<>();
        res.put("bookList", bookList);
        res.put("pageInfo", pageInfo);

        return Result.success(res);

    }

    public Result addOneBookInfo(Map<String, Object> json) {
        // System.out.println(json);
        // 存储作者信息
        Book exist = getBookByName((String) json.get("book_title"));
        if (exist != null) {
            return Result.success("书籍已存在");
        }
        Author author = new Author();
        author.setName((String) json.get("author"));
        author.setTitle((String) json.get("author_title"));
        author.setUrl((String) json.get("author_url"));
        Author addAuthor = authorService.addAuthor(author);

        // 存储分类信息
        Classify classify = new Classify();
        classify.setName((String) json.get("book_classify"));
        classify.setUrl((String) json.get("book_classify_url"));
        Classify addClassify = classifyService.addClassify(classify);
        Long classifyId = addClassify.getId();


        // 存储标签信息
        List<Map<String, String>> tagInfo = (List<Map<String, String>>) json.get("tag_info");
        List<Long> tagIds = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        for (Map<String, String> oneTag : tagInfo) {
            Tag tag = new Tag();
            tag.setName(oneTag.get("tag"));
            tag.setUrl(oneTag.get("url"));
            Tag addTag = tagService.addTag(tag);
            tagIds.add(addTag.getId());
            tagNames.add(addTag.getName());
        }

        // 存储书籍信息，记录作者id
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Book book = new Book();
            book.setAuthorId(addAuthor.getId());
            book.setFileSize((String) json.get("size"));
            book.setStar((String) json.get("star_level"));
            Date date = dateFormat.parse((String) json.get("finish_time"));
            book.setFinishTime(date);
            book.setNewName((String) json.get("book_title"));
            book.setBookUrl((String) json.get("book_url"));
            book.setClassifyId(classifyId);
            book.setTag(String.join(",", tagNames));
            book.setInformation((String) json.get("book_info"));
            Map<String, String> downInfo = (Map<String, String>) json.get("down_info");
            book.setDownUrl(downInfo.get("href"));
            Book addBook = addBook(book);
            // 将书籍与分类、标签关联
            bookClassifyService.addAllBookClassify(addBook.getId(), classifyId, tagIds);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

    public Map<String, Object> getBookInfo(Long id) {
        Book book = bookRepository.getById(id);
        Map<String, Object> res = new HashMap<>();
        res.put("bookName", book.getNewName());
        res.put("bookFinishTime", book.getFinishTime());
        res.put("bookStar", book.getStar());
        res.put("bookFileSize", book.getFileSize());
        if (book.getClassifyId() != null) {
            Classify classify = classifyService.getClassifyById(book.getClassifyId());
            res.put("bookClassify", classify.getName());
            res.put("bookClassifyUrl", BASE_URL + classify.getUrl());
        }
        if (book.getAuthorId() != null) {
            Author author = authorService.getAuthorById(book.getAuthorId());
            res.put("bookAuthor", author.getName());
            res.put("bookAuthorUrl", BASE_URL + author.getUrl());
        }
        if (book.getTypeId() != null) {
            res.put("bookType", typeService.getTypeById(book.getTypeId()).getName());
        }
        res.put("bookTag", book.getTag());
        res.put("evaluate", book.getEvaluate());
        res.put("information", book.getInformation());
        res.put("bookUrl", book.getBookUrl());
        res.put("bookDownUrl", BASE_URL + book.getDownUrl());
        return res;
    }

}
