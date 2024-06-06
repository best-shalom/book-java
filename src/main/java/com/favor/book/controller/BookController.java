package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Book;
import com.favor.book.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author Administrator
 * 尽量统一使controller层直接return，即在service层返回Result对象，而不是直接返回对象，其中如果有些复用的需要提供对象的再考虑在controller层处理
 * 因为这样的话，方法的报错信息这些可以直接在service层根据处理情况添加
 */
@Controller
@RequestMapping("/book")
public class BookController {
    @Resource
    BookService bookService;

    /**
     * 新增单本书籍
     * @param file 书籍文件
     * @return 返回是否成功
     */
    @RequestMapping(value = "/addOneBook",method = RequestMethod.POST)
    @ResponseBody
    public Result addOneBook(MultipartFile file, String newName) {
        return bookService.addOneBookFile(file, newName);
    }

    @RequestMapping(value = "/downloadOneBook", method = RequestMethod.POST)
    @ResponseBody
    public Result downloadOneBook(Long id, String savePath) {
        return bookService.downloadOneBookFile(id, savePath);
    }

    /**
     * 注解@RequestBody:是 Spring MVC 中的一个注解，用于将方法的返回值直接写入 HTTP 响应体中，而不是通过视图解析器渲染成页面。
     * 它通常用于构建 RESTful 服务或者返回 JSON、XML 等格式的数据给客户端。
     * ！！！如果不使用RequestBody注解，接口返回结果就没有办法正确被前端、apiFox等识别，从而报错404，这是一个很容易误解的问题！！！
     *
     * @param json python爬虫获取的json格式的书籍信息
     * @return 返回添加结果
     */
    @RequestMapping(value = "/addOneBookInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result addOneBookInfo(@RequestBody Map<String, Object> json) {
        return bookService.addOneBookInfo(json);
    }

    /**
     * 按条件过滤书籍列表
     * </p>
     * 在请求中只需要在方法的参数中直接定义一个pageable类型的参数，当Spring发现这个参数时，Spring会自动的根据request的参数来组装该pageable对象。
     * Spring支持的request参数如下：
     * page ：第几页，从0开始，默认为第0页
     * size ：每一页的大小，默认为10
     * sort ：排序相关的信息，以`property[,ASC|DESC]`的方式组织，例如`sort=firstname&sort=lastname,desc`表
     * @return 返回分页筛选结果
     */
    @RequestMapping(value = "listBooks", method = RequestMethod.POST)
    @ResponseBody
    public Result listBooks(@PageableDefault() Pageable pageable, String classifyName, String tagName, String typeName, int orderByUpload, int orderByFinish) {
        return bookService.listBooks(pageable, classifyName, tagName, typeName, orderByUpload, orderByFinish);
    }

    /**
     * 使用 @RequestParam 注解来映射请求参数
     *
     * @param id 书籍id
     * @return 返回单本书籍信息
     */
    @RequestMapping(value = "getBookById", method = RequestMethod.GET)
    @ResponseBody
    public Result getBookById(@RequestParam Long id) {
        Book res = bookService.getBookById(id);
        return Result.success(res);
    }

    @RequestMapping(value = "updateBookInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result updateBookInfo(Long bookId, String typeName, String classifyName, String evaluate) {
        return bookService.updateBookInfo(bookId, typeName, classifyName, evaluate);
    }
}
