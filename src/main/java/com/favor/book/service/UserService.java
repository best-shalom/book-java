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


    public String login(String account,String password){
        //通过map条件查询用户信息
        Map<String,Object> map=new HashMap<>();
        map.put("account",account);
        List<User> userList=userRepository.selectByMap(map);
        if(userList.isEmpty()){
            return "账号不存在";
        }
        if( userList.get(0).getPassword().equals(password)){
            return "登录成功";
        }

        return "密码错误";
    }
}
