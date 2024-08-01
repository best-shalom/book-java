package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.TagRepository;
import com.favor.book.entity.Tag;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CQ
 * @version 1.0
 */

@Service
public class TagService {

    @Resource
    private TagRepository tagRepository;

    public Result getAllTags() {
        return Result.success(tagRepository.findAll());
    }

    public Result getTagById(Long id) {
        return Result.success(tagRepository.findById(id));
    }

    public Tag getTagByName(String name) {
        // 将查询结果封装在一个 Optional 对象中，然后使用 ifPresent 方法来执行当查询结果非空时的操作。
        Optional<Tag> tagOptional = tagRepository.findByName(name);
        return tagOptional.orElse(null);
    }

    public Long getTagIdByName(String name) {
        // 将查询结果封装在一个 Optional 对象中，然后使用 ifPresent 方法来执行当查询结果非空时的操作。
        Optional<Tag> tagOptional = tagRepository.findByName(name);
        return tagOptional.map(Tag::getId).orElse(null);
    }

    /**
     * 先查重，再存入
     */
    public Tag addTag(Tag tag) {
        Tag exist = getTagByName(tag.getName());
        if (exist != null) {
            return exist;

        }
        return tagRepository.save(tag);
    }

    public Result deleteTag(Long id) {
        tagRepository.deleteById(id);
        return Result.success();
    }

    /**
     * 先查再复制更新
     *
     * @param tag 前端传入的实体
     * @return 返回更新结果
     */
    public Result updateTag(Tag tag) {
        Optional<Tag> optionalTag = tagRepository.findById(tag.getId());
        if (optionalTag.isEmpty()) {
            return Result.error("标签不存在");
        }
        Tag newTag = optionalTag.get();
        newTag.setName(tag.getName());
        newTag.setInformation(tag.getInformation());
        newTag.setUpdateTime(new Date());
        return Result.success(tagRepository.save(newTag));
    }

    public List<String> showAllTagName() {
        List<Tag> tagList = tagRepository.findAll();
        // 在Java中，.map(Tag::getFieldName) 使用了方法引用的方式，将 Tag 对象的方法 getFieldName() 应用到流中的每个元素上。
        // 而在Tag对象中，通过getter和setter实现了对应的参数获取方法，即getName()等
        return tagList.stream().map(Tag::getName).collect(Collectors.toList());
    }
}
