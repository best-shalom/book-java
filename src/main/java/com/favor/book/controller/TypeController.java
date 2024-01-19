package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.entity.Type;
import com.favor.book.service.TypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Optional<Type> getTypeById(@RequestParam Long id) {
        return typeService.getTypeById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result addType(@RequestParam String name, @RequestParam String information) {
        return typeService.addType(name, information);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result deleteType(@RequestParam Long id) {
        return typeService.deleteType(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result updateType(@RequestBody Type type) {
        return typeService.updateType(type);
    }
}
