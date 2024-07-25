package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.BookRepository;
import com.favor.book.entity.*;
import com.favor.book.utils.FileUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
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
     * 按照小说类型or阅读类型筛选小说
     *
     * @return 返回指定排序方式的小说列表
     */
    public Result listBooks(Pageable pageable, Map<String, Object> json) {
        QBook book = QBook.book;
        Map<String, Object> filterInfo = json.containsKey("filterInfo") && json.get("filterInfo") instanceof Map ? (Map<String, Object>) json.get("filterInfo") : Collections.emptyMap();

        Map<String, Object> searchInfo = json.containsKey("searchInfo") && json.get("searchInfo") instanceof Map ? (Map<String, Object>) json.get("searchInfo") : Collections.emptyMap();

        Map<String, Object> rangeInfo = json.containsKey("rangeInfo") && json.get("rangeInfo") instanceof Map ? (Map<String, Object>) json.get("rangeInfo") : Collections.emptyMap();

        Map<String, Object> sortInfo = json.containsKey("sortInfo") && json.get("sortInfo") instanceof Map ? (Map<String, Object>) json.get("sortInfo") : Collections.emptyMap();

        // 构建查询条件
        BooleanBuilder predicate = new BooleanBuilder();
        buildFilterPredicate(predicate, filterInfo);

        // 处理搜索条件
        buildSearchPredicate(predicate, searchInfo);

        // 处理范围条件
        buildRangePredicate(predicate, rangeInfo);

        // 处理排序条件
        OrderSpecifier<Date> orderByField = buildSortExpression(sortInfo);
        // page的页面下标从0开始
        // jpaQueryFactory 是 QueryDSL 的 JPAQueryFactory 实例，用于创建和执行查询。
        // selectFrom(book) 指定要从 Book 表中查询数据。
        List<Book> allBooks = jpaQueryFactory.selectFrom(book)
                .where(predicate).orderBy(orderByField)
                .fetch();
        // 计算总记录数
        long totalCount = allBooks.size();
        // 计算总页数
        long totalPages = (totalCount + pageable.getPageSize() - 1) / pageable.getPageSize();
        // 分页
        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min((int) (pageable.getOffset() + pageable.getPageSize()), allBooks.size());
        List<Book> bookList = allBooks.subList(fromIndex, toIndex);
        List<Map<String, Object>> bookResList = bookList.stream().map(bookInstance -> {
            Map<String, Object> res = new HashMap<>();
            res.put("bookId", bookInstance.getId());
            res.put("bookName", bookInstance.getNewName());
            res.put("bookFinishTime", bookInstance.getFinishTime().toString());
            res.put("bookStar", bookInstance.getStar());
            res.put("bookFileSize", bookInstance.getFileSize());
            if (bookInstance.getAuthorId() != null) {
                Author author = authorService.getAuthorById(bookInstance.getAuthorId());
                res.put("bookAuthor", author.getName());
                res.put("bookAuthorUrl", BASE_URL + author.getUrl());
            }
            res.put("bookTag", bookInstance.getTag());
            res.put("bookBeforeScore", bookInstance.getBeforeScore());
            res.put("bookAfterScore", bookInstance.getAfterScore());
            res.put("bookInformation", bookInstance.getInformation());
            return res;
        }).collect(Collectors.toList());

        // 构建page信息
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageNumber", pageable.getPageNumber());
        pageInfo.put("pageSize", pageable.getPageSize());
        pageInfo.put("totalPages", totalPages);
        pageInfo.put("totalCount", totalCount);

        Map<String, Object> res = new HashMap<>();
        res.put("bookList", bookResList);
        res.put("pageInfo", pageInfo);

        return Result.success(res);

    }

    /**
     * 构建过滤条件的 Predicate
     *
     * @param predicate  传入
     * @param filterInfo 参数
     */
    private void buildFilterPredicate(BooleanBuilder predicate, Map<String, Object> filterInfo) {
        String classifyName = filterInfo.containsKey("classifyName") ? (String) filterInfo.get("classifyName") : null;
        String tagName = filterInfo.containsKey("tagName") ? (String) filterInfo.get("tagName") : null;
        String typeName = filterInfo.containsKey("typeName") ? (String) filterInfo.get("typeName") : null;

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
        // BooleanExpression predicate = book.isDeleted.eq(0);
        // 执行动态条件拼装
        if (classifyIdList != null && !classifyIdList.isEmpty()) {
            // 构建一个条件，筛选分类 ID 在 classifyIdList 中的书籍。predicate.and(...) 将这个条件与之前的条件组合，要求同时满足
            predicate.and(book.classifyId.in(classifyIdList));
        }
        if (tagNameList != null && !tagNameList.isEmpty()) {
            // 使用 tagNameList.stream() 将标签名称列表转换为流（stream）。
            // map(book.tag::contains) 将每个标签名称映射为一个条件，检查书籍的标签字段是否包含该标签名称。
            // .reduce(BooleanExpression::and) 将这些条件使用逻辑 and 组合成一个条件，即标签字段必须包含所有标签名称。
            // orElse(Expressions.FALSE.isFalse()) 如果没有标签名称，则返回一个永远为 false 的条件。
            // predicate = predicate.and(tagExpression) 将这个条件与之前的条件组合，要求同时满足。
            BooleanExpression tagExpression = tagNameList.stream().map(book.tag::contains).reduce(BooleanExpression::and).orElse(Expressions.FALSE.isFalse());
            predicate.and(tagExpression);
        }
        if (typeIdList != null && !typeIdList.isEmpty()) {
            predicate.and(book.typeId.in(typeIdList));
        }
    }

    /**
     * 构建搜索条件的 Predicate
     *
     * @param predicate  传入
     * @param searchInfo 参数
     */
    private void buildSearchPredicate(BooleanBuilder predicate, Map<String, Object> searchInfo) {
        QBook book = QBook.book;
        String bookName = searchInfo.containsKey("bookName") ? (String) searchInfo.get("bookName") : null;
        String authorName = searchInfo.containsKey("authorName") ? (String) searchInfo.get("authorName") : null;
        String mainCharacterName = searchInfo.containsKey("mainCharacterName") ? (String) searchInfo.get("mainCharacterName") : null;
        String content = searchInfo.containsKey("content") ? (String) searchInfo.get("content") : null;
        boolean bookNameFuzzy = searchInfo.containsKey("bookNameFuzzy") && (boolean) searchInfo.get("bookNameFuzzy");
        boolean contentFuzzy = searchInfo.containsKey("contentFuzzy") && (boolean) searchInfo.get("contentFuzzy");

        if (bookName != null && !bookName.isEmpty()) {
            predicate.and(bookNameFuzzy ? book.newName.containsIgnoreCase(bookName) : book.newName.eq(bookName));
        }
        // 根据作者名称找到作者的id，判断书籍是否符合要求
        if (authorName != null && !authorName.isEmpty()) {
            predicate.and(book.authorId.in(authorService.getAuthorByName(authorName).getId()));
        }
        if (mainCharacterName != null && !mainCharacterName.isEmpty()) {
            predicate.and(book.information.containsIgnoreCase(mainCharacterName));
        }
        // TODO:结合ES框架，实现搜索（模糊搜索）
        if (content != null && !content.isEmpty()) {
            predicate.and(contentFuzzy ? book.information.containsIgnoreCase(content) : book.information.eq(content));
        }
    }

    /**
     * 构建范围条件的 Predicate
     *
     * @param predicate 传入
     * @param rangeInfo 参数
     */
    private void buildRangePredicate(BooleanBuilder predicate, Map<String, Object> rangeInfo) {
        QBook book = QBook.book;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date finishTimeFrom = rangeInfo.containsKey("finishTimeFrom") ? dateFormat.parse((String) rangeInfo.get("finishTimeFrom")) : null;
            Date finishTimeTo = rangeInfo.containsKey("finishTimeTo") ? dateFormat.parse((String) rangeInfo.get("finishTimeTo")) : null;
            int scoreMin = rangeInfo.containsKey("scoreMin") ? (int) rangeInfo.get("scoreMin") : 0;
            int scoreMax = rangeInfo.containsKey("scoreMax") ? (int) rangeInfo.get("scoreMax") : 100;
            if (finishTimeFrom != null && finishTimeTo != null) {
                predicate.and(book.finishTime.between(finishTimeFrom, finishTimeTo));
            } else if (finishTimeFrom != null) {
                predicate.and(book.finishTime.goe(finishTimeFrom));
            } else if (finishTimeTo != null) {
                predicate.and(book.finishTime.loe(finishTimeTo));
            }
            // TODO:增加书籍的各种评分字段：阅读前评分、阅读后评分
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建排序条件的表达式
     * 0-升序 1-降序 2-不排序
     * 排序优先级-完结时间、上传时间、评分
     *
     * @param sortInfo 参数
     * @return 返回排序表达式
     */
    private OrderSpecifier<Date> buildSortExpression(Map<String, Object> sortInfo) {
        QBook book = QBook.book;
        // 在强制转换为 Integer 之前，先检查对象是否确实是 Integer 的实例。
        // 如果不是 Integer，则假设它可能是一个 String，并尝试使用 Integer.parseInt() 进行转换。
        int orderByFinish = sortInfo.containsKey("orderByFinish") ? (sortInfo.get("orderByFinish") instanceof Integer ? (Integer) sortInfo.get("orderByFinish") : Integer.parseInt((String) sortInfo.get("orderByFinish"))) : 2;
        int orderByUpload = sortInfo.containsKey("orderByUpload") ? (sortInfo.get("orderByUpload") instanceof Integer ? (Integer) sortInfo.get("orderByUpload") : Integer.parseInt((String) sortInfo.get("orderByUpload"))) : 2;
        int orderByScore = sortInfo.containsKey("orderByScore") ? (sortInfo.get("orderByScore") instanceof Integer ? (Integer) sortInfo.get("orderByScore") : Integer.parseInt((String) sortInfo.get("orderByScore"))) : 2;

        if (orderByFinish == 0) {
            return book.finishTime.asc();
        } else if (orderByFinish == 1) {
            return book.finishTime.desc();
        }
        if (orderByUpload == 0) {
            return book.createTime.asc();
        } else if (orderByUpload == 1) {
            return book.createTime.desc();
        }
        // TODO：基于书籍的某项评分排序
        else {
            // 默认按创建时间升序（即新上传的在前）
            return book.createTime.asc();
        }
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
        res.put("bookFinishTime", book.getFinishTime().toString());
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
        res.put("beforeScore", book.getBeforeScore());
        res.put("afterScore", book.getAfterScore());
        res.put("information", book.getInformation());
        res.put("bookUrl", book.getBookUrl());
        res.put("bookDownUrl", BASE_URL + book.getDownUrl());
        return res;
    }

    /**
     * 根据选择的参数更新书籍信息
     */
    public Result updateBookInfo(Map<String, Object> json) {
        Long bookId = Long.parseLong(json.get("bookId").toString());
        String typeName = json.get("typeName") == null ? null : json.get("typeName").toString();
        String classifyName = json.get("classifyName") == null ? null : json.get("classifyName").toString();
        String tagNames = json.get("tagNames") == null ? null : json.get("tagNames").toString();
        List<String> tagNameList = tagNames  != null ? Stream.of(tagNames.split(",")).map(String::trim).filter(name -> !name.isEmpty()).toList() : null;

        Double beforeScore = json.get("beforeScore") == null ? null : Double.parseDouble(json.get("beforeScore").toString());
        Double afterScore = json.get("afterScore") == null ? null : Double.parseDouble(json.get("afterScore").toString());
        String evaluate = json.get("evaluate") == null ? null : json.get("evaluate").toString();

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
        if (tagNames != null) {
            for (String tagName : tagNameList) {
                Long tagId = tagService.getTagIdByName(tagName);
                if (tagId == null) {
                    return Result.error("小说标签不存在");
                }
                // 添加书籍与标签的关联
                bookClassifyService.addBookClassifyTag(bookId, book.getClassifyId(),tagId);
            }
            book.setTag(tagNames);
        }
        if (beforeScore != null) {
            book.setBeforeScore(beforeScore);
        }
        if (afterScore != null) {
            book.setAfterScore(afterScore);
        }
        if (evaluate != null) {
            book.setEvaluate(evaluate);
        }
        return Result.success(bookRepository.save(book));
    }
}
