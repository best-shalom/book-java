package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Author;
import com.favor.book.service.AuthorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Resource
    private AuthorService authorService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    public Result getAuthorById(Long id) {
        return authorService.getAuthorById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addAuthor(Author author) {
        return Result.success(authorService.addAuthor(author));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result deleteAuthor(Long id) {
        return authorService.deleteAuthor(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updateAuthor(Author author) {
        return authorService.updateAuthor(author);
    }
}