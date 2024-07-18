package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Classify;
import com.favor.book.service.ClassifyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/classify")
public class ClassifyController {

    @Resource
    private ClassifyService classifyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result getAllClassifies() {
        return classifyService.getAllClassifies();
    }

    @RequestMapping(value = "/getById", method = RequestMethod.POST)
    public Result getClassifyById(Long id) {
        return Result.success(classifyService.getClassifyById(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addClassify(Classify classify) {
        if (classify.getName().isEmpty()) {
            return Result.error("分类名称不能为空");
        }
        Classify exist = classifyService.getClassifyByName(classify.getName());
        if (exist != null) {
            return Result.error("分类名称已存在");
        }
        return Result.success(classifyService.addClassify(classify));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result deleteClassify(Long id) {
        return classifyService.deleteClassify(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updateClassify(Classify classify) {
        return classifyService.updateClassify(classify);
    }

    @RequestMapping(value = "/showAllName", method = RequestMethod.GET)
    public Result showAllClassifyName() {
        return Result.success(classifyService.showAllClassifyName());
    }

}

