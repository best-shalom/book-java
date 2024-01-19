package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Book;
import com.favor.book.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * @author Administrator
 */
@Controller
@RequestMapping("/book")
public class BookController {
    @Resource
    BookService bookService;

    @RequestMapping(value = "/addOneBook",method = RequestMethod.POST)
    @ResponseBody
    public String addOneBook(MultipartFile file){
        return bookService.addOneBook(file);
    }

    /**
     * 在请求中只需要在方法的参数中直接定义一个pageable类型的参数，当Spring发现这个参数时，Spring会自动的根据request的参数来组装该pageable对象。
     * Spring支持的request参数如下：
     * page ：第几页，从0开始，默认为第0页
     * size ：每一页的大小，默认为10
     * sort ：排序相关的信息，以`property[,ASC|DESC]`的方式组织，例如`sort=firstname&sort=lastname,desc`表
     *
     * @return 分页筛选结果
     */
    @RequestMapping(value = "listBooks", method = RequestMethod.POST)
    @ResponseBody
    public Result listBooks(@PageableDefault() Pageable pageable, Long classifyId, Long typeId) {
        Page<Book> res = bookService.listBooks(pageable, classifyId, typeId);
        return Result.success(res);

    }
}
