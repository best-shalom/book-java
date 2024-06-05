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

    public Result getTypeById(Long id) {
        return Result.success(typeRepository.findById(id));
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
