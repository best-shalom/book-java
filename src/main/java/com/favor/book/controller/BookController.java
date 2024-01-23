package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Book;
import com.favor.book.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author Administrator
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
        return bookService.addOneBook(file, newName);
    }

    @RequestMapping(value = "/downloadOneBook", method = RequestMethod.POST)
    @ResponseBody
    public Result downloadOneBook(Long id, String savePath) {
        return bookService.downloadOneBook(id, savePath);
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
    public Result listBooks(@PageableDefault() Pageable pageable, Long classifyId, Long typeId, int orderByUpload, int orderByFinish) {
        List<Book> res = bookService.listBooks(pageable, classifyId, typeId, orderByUpload, orderByFinish);
        return Result.success(res);

    }
}
