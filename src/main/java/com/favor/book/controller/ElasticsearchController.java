package com.favor.book.controller;

import com.favor.book.config.ElasticsearchConfig;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.io.IOException;

/**
 * @author CQ
 * @version 1.0
 * @date 2024/7/15 20:47
 * @description ES框架接口
 */

@RestController
@RequestMapping("/es")
public class ElasticsearchController {
    @Resource
    ElasticsearchConfig elasticsearchConfig;
    @RequestMapping("/test")
    public void test() throws IOException {

    }
}
