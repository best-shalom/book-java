package com.favor.book.service;


import com.favor.book.common.Result;
import com.favor.book.dao.AuthorRepository;
import com.favor.book.entity.Author;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author Administrator
 */
@Service
public class AuthorService {

    @Resource
    private AuthorRepository authorRepository;

    public Result getAllAuthors() {
        return Result.success(authorRepository.findAll());
    }

    public Author getAuthorById(Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        return optionalAuthor.orElse(null);
    }

    public Author getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    /**
     * 存入作者信息，如果作者存在，返回已存在的作者信息；否则存入作者信息并返回存入结果
     */
    public Author addAuthor(Author author) {
        Author exist = getAuthorByName(author.getName());
        if (exist != null) {
            return exist;
        }
        return authorRepository.save(author);
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
