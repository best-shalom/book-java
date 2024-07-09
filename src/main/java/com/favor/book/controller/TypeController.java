package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Type;
import com.favor.book.service.TypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAllTypes() {
        return typeService.getAllTypes();
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    public Result getTypeById(Long id) {
        return Result.success(typeService.getTypeById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addType(Type type) {
        return typeService.addType(type);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result deleteType(Long id) {
        return typeService.deleteType(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updateType(Type type) {
        return typeService.updateType(type);
    }
}
