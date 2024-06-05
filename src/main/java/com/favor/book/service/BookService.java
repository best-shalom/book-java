package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.BookRepository;
import com.favor.book.entity.*;
import com.favor.book.utils.FileUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
     */
    public Result updateBookInfo(Long bookId, String typeName, String classifyName) {
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
        Long classifyId = classifyService.getClassifyIdByName(classifyName);
        Long tagId = tagService.getTagIdByName(tagName);
        Long typeId = typeService.getTypeIdByName(typeName);

        // Pageable 是Spring Data库中定义的一个接口，用于构造翻页查询，是所有分页相关信息的一个抽象，通过该接口，我们可以得到和分页相关所有信息（例如pageNumber、pageSize等），这样，Jpa就能够通过pageable参数来得到一个带分页信息的Sql语句。
        QBook book = QBook.book;
        // 初始化组装条件(book.isDeleted为0，即未删除的书籍)
        Predicate predicate = book.isDeleted.eq(0);
        // 执行动态条件拼装：如果参数==null，则predicate=predicate不变，否则就使用ExpressionUtils构建查询条件
        predicate = classifyId == null ? predicate : ExpressionUtils.and(predicate, book.classifyId.eq(classifyId));
        // 标签可以用模糊查询，也可以从关联表中匹配
        predicate = tagId == null ? predicate : ExpressionUtils.and(predicate, book.tag.contains(tagName));
        predicate = typeId == null ? predicate : ExpressionUtils.and(predicate, book.typeId.eq(typeId));
        // page的页面下标从0开始
        // 查询所有符合条件的记录
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

}
