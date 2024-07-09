package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.TypeRepository;
import com.favor.book.entity.Type;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author Administrator
 */
@Service
public class TypeService {

    @Resource
    private TypeRepository typeRepository;

    public Result getAllTypes() {
        return Result.success(typeRepository.findAll());
    }

    public Type getTypeById(Long id) {
        Optional<Type> typeOptional = typeRepository.findById(id);
        return typeOptional.orElse(null);
    }

    public Type getTypeByName(String name) {
        Optional<Type> typeOptional = typeRepository.findFirstByName(name);
        return typeOptional.orElse(null);
    }

    public Long getTypeIdByName(String name) {
        Type type = getTypeByName(name);
        return type == null ? null : type.getId();
    }

    public Result addType(Type type) {
        /*
        isEmpty: 判断对象是否为空。
        对于字符串：
            如果字符串的长度为 0，则返回 true。
            如果字符串不为 null 且包含除空格外的字符，则返回 false。
        对于集合（如 List、Set 等）：
            如果集合中不包含任何元素，则返回 true。
            如果集合不为 null 且包含元素，则返回 false。
        对于数组：
            如果数组的长度为 0，则返回 true。
            如果数组不为 null 且包含元素，则返回 false。
         */
        if (type.getName().isEmpty()) {
            return Result.error("类型名称不能为空");
        }
        if (typeRepository.findFirstByName(type.getName()).isPresent()) {
            return Result.error("类型已存在");
        }
        return Result.success(typeRepository.save(type));
    }

    public Result deleteType(Long id) {
        typeRepository.deleteById(id);
        return Result.success();
    }

    public Result updateType(Type type) {
        Optional<Type> optionalType = typeRepository.findById(type.getId());
        if (!optionalType.isPresent()) {
            return Result.error("类型不存在");
        }
        Type newType = optionalType.get();
        newType.setName(type.getName());
        newType.setInformation(type.getInformation());
        newType.setUpdateTime(new Date());
        return Result.success(typeRepository.save(newType));
    }
}
