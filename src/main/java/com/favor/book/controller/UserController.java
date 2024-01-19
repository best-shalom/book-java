package com.favor.book.controller;

import com.favor.book.common.Result;
import com.favor.book.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 1.声明为Controller方法
 * 2.方法的基础路径为/user
 * @author Administrator
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    // TODO 使用userService方法，但要用@Resource标注，@Resource的作用？
    //  同时UserService上也要使用@Component？作用？
    @Resource
    UserService userService;

    /**
     * 1.RequestMapping注解：访问地址localhost:8080/user/login
     * 2.ResponseBody注解
     * 接受参数名为account和password
     * @param account 账号
     * @param password 密码
     * @return 登录结果
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam(name = "account") String account, @RequestParam(name = "password") String password) {
        return userService.login(account,password);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestParam(name = "account") String account, @RequestParam(name = "password") String password) {
        return userService.register(account,password);
    }
}
