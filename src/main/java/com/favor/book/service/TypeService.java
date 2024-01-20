package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.TypeRepository;
import com.favor.book.entity.Type;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 */
@Service
public class TypeService {

    @Resource
    private TypeRepository typeRepository;

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Optional<Type> getTypeById(Long id) {
        return typeRepository.findById(id);
    }

    public Result addType(Type type) {
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
