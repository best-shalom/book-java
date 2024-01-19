package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Author;
import com.favor.book.service.AuthorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Resource
    private AuthorService authorService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Optional<Author> getAuthorById(Long id) {
        return authorService.getAuthorById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addAuthor(String name, String information) {
        return authorService.addAuthor(name, information);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result deleteAuthor(Long id) {
        return authorService.deleteAuthor(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result updateAuthor(Author author) {
        return authorService.updateAuthor(author);
    }
}