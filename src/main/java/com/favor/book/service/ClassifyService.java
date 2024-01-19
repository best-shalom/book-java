package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.ClassifyRepository;
import com.favor.book.entity.Classify;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
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

    public Result getClassifyById(Long id) {
        return Result.success(classifyRepository.findById(id));
    }

    public Result addClassify(Classify classify) {
        return Result.success(classifyRepository.save(classify));
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
        if (!optionalClassify.isPresent()) {
            return Result.error("分组不存在");
        }
        Classify newClassify = optionalClassify.get();
        newClassify.setName(classify.getName());
        newClassify.setInformation(classify.getInformation());
        newClassify.setUpdateTime(new Date());
        return Result.success(classifyRepository.save(newClassify));
    }
}

