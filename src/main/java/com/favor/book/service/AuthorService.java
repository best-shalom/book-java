package com.favor.book.service;


import com.favor.book.common.Result;
import com.favor.book.dao.AuthorRepository;
import com.favor.book.entity.Author;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 */
@Service
public class AuthorService {

    @Resource
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Result addAuthor(String name, String information) {
        Author author = new Author();
        author.setName(name);
        author.setInformation(information);
        return Result.success(authorRepository.save(author));
    }

    public Result deleteAuthor(Long id) {
        authorRepository.deleteById(id);
        return Result.success();
    }

    public Result updateAuthor(Author author) {
        Optional<Author> optionalAuthor = authorRepository.findById(author.getId());
        if (!optionalAuthor.isPresent()) {
            return Result.error("作者不存在");
        }
        Author newAuthor = optionalAuthor.get();
        newAuthor.setName(author.getName());
        newAuthor.setInformation(author.getInformation());
        newAuthor.setUpdateTime(new Date());
        return Result.success(authorRepository.save(newAuthor));
    }
}
