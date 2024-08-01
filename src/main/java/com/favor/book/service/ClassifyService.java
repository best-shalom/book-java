package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.ClassifyRepository;
import com.favor.book.entity.Classify;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 书籍分组信息
 * @author Administrator
 */
@Service
public class ClassifyService {

    @Resource
    private ClassifyRepository classifyRepository;

    public Result getAllClassifies() {
        return Result.success(classifyRepository.findAll());
    }

    public Classify getClassifyById(Long id) {
        Optional<Classify> classifyOptional = classifyRepository.findById(id);
        return classifyOptional.orElse(null);
    }

    public Classify getClassifyByName(String name) {
        Optional<Classify> classifyOptional = classifyRepository.findByName(name);
        return classifyOptional.orElse(null);
    }

    public Long getClassifyIdByName(String name) {
        Optional<Classify> classifyOptional = classifyRepository.findByName(name);
        return classifyOptional.map(Classify::getId).orElse(null);
    }

    public List<Long> getClassifyIdsByNames(List<String> nameList) {
        List<Long> classifyIdList = new ArrayList<>();
        for (String name : nameList) {
            classifyIdList.add(getClassifyByName(name).getId());
        }
        return classifyIdList;
    }

    /**
     * 先查重，再存入
     */
    public Classify addClassify(Classify classify) {
        Classify exist = getClassifyByName(classify.getName());
        if (exist != null) {
            return exist;

        }
        return classifyRepository.save(classify);
    }

    public Result deleteClassify(Long id) {
        classifyRepository.deleteById(id);
        return Result.success();
    }

    /**
     * 先查再复制更新
     *
     * @param classify 前端传入的实体
     * @return 返回更新结果
     */
    public Result updateClassify(Classify classify) {
        Optional<Classify> optionalClassify = classifyRepository.findById(classify.getId());
        if (optionalClassify.isEmpty()) {
            return Result.error("分组不存在");
        }
        Classify newClassify = optionalClassify.get();
        newClassify.setName(classify.getName());
        newClassify.setInformation(classify.getInformation());
        newClassify.setUpdateTime(new Date());
        return Result.success(classifyRepository.save(newClassify));
    }

    public List<String> showAllClassifyName() {
        List<Classify> classifyList = classifyRepository.findAll();
        // 在Java中，.map(Classify::getFieldName) 使用了方法引用的方式，将 Classify 对象的方法 getFieldName() 应用到流中的每个元素上。
        // 而在Classify对象中，通过getter和setter实现了对应的参数获取方法，即getName()等
        return classifyList.stream().map(Classify::getName).collect(Collectors.toList());
    }
}

