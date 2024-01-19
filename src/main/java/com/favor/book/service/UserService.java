package com.favor.book.service;

import com.favor.book.dao.UserRepository;
import com.favor.book.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    UserRepository userRepository;


    public User getUserByAccount(String account){
        Map<String,Object> map=new HashMap<>();
        map.put("account",account);
        List<User> userList=userRepository.selectByMap(map);
        if(userList.isEmpty()){
            return null;
        }
        return userList.get(0);
    }
    public String login(String account,String password){
        //通过map条件查询用户信息
        User user=getUserByAccount(account);
        if(user==null){
            return "账号不存在";
        }
        if(user.getPassword().equals(password)){
            return "登录成功";
        }
        return "密码错误";
    }

    public String register(String account,String password){
        User user=getUserByAccount(account);
        if(user!=null){
            return "账号已存在，请更换";
        }
        user=new User();
        user.setAccount(account);
        user.setPassword(password);
        // insert的返回结果为数据库表改变的条数
        int result=userRepository.insert(user);
        if(result==0){
            return "注册失败";
        }
        return "注册成功";
    }
}
