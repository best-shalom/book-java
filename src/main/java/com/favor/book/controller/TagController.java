package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.service.TagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * @author CQ
 * @version 1.0
 */

@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    TagService tagService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAllTags() {
        return tagService.getAllTags();
    }
}
