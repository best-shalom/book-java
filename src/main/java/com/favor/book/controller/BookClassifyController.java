package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.service.BookClassifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bookClassify")
public class BookClassifyController {
    @Resource
    BookClassifyService bookClassifyService;

    @RequestMapping(value = "/getBookByClassify", method = RequestMethod.GET)
    @ResponseBody
    public Result getBookByClassify(@RequestBody Map<String, List<String>> json) {
        return Result.success(bookClassifyService.getBookByClassifyName(json.get("classifyNameList")));
    }
}
