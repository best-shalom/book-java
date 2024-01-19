package com.favor.book.controller;

import com.favor.book.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


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
}
