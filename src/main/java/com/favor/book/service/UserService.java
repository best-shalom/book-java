package com.favor.book.service;

import com.favor.book.common.Result;
import com.favor.book.dao.UserRepository;
import com.favor.book.entity.QUser;
import com.favor.book.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    JPAQueryFactory jpaQueryFactory;


    public User getUserByAccount(String account){
        QUser user = QUser.user;
        List<User> exist = jpaQueryFactory.selectFrom(user)
                // 查询对象
                .where(user.account.eq(account)).fetch();
        if (exist.isEmpty()) {
            return null;
        }
        return exist.get(0);
    }

    public Result login(String account, String password) {
        //通过map条件查询用户信息
        User user=getUserByAccount(account);
        if(user==null){
            return Result.error("账号不存在");
        }
        if (!user.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        return Result.success("登录成功");
    }

    public Result register(String account, String password) {
        User user=getUserByAccount(account);
        if(user!=null){
            return Result.error("用户已存在");
        }
        user=new User();
        // 账号作为默认用户名
        user.setName(account);
        user.setAccount(account);
        user.setPassword(password);
        // insert的返回结果为数据库表改变的条数
        return Result.success(userRepository.save(user));
    }
}
