package com.snake.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snake.pojo.User;
import com.snake.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    //记录日志
    private final Logger logger = Logger.getLogger(UserController.class);
    //注入UserService
    @Autowired
    private UserService userService;

    /**
     * 2022年6月28日17:47:26
     * 判断用户输入的用户名是否已经存在  application/json text/html
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/userIsExist", produces = {"text/html;charset=utf-8"}) //指定返回数据类型
    @ResponseBody //设置返回数据是响应体,直接返回数据本身
    public String userIsExist(User user) {
        logger.info("查找的用户名是" + user.getUsername());
        User queryResult = userService.findUserByUser(user);
        return "{\"isExist\":" + (queryResult != null) + "}"; //true代表用户存在, false 代表不存在
    }

    /**
     * 2022年6月29日16:00:25
     * 根据用户数据进行注册账号
     * @param user
     * @return
     */
    @RequestMapping(value = "/userRegister",produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String userRegister(User user){
        logger.info("注册用户名是:" + user.getUsername());
        logger.info("注册密码是:" + user.getPassword());
        int effectLine = userService.registerByUser(user);
        logger.info("影响行数:"+effectLine);
        return Boolean.toString(effectLine == 1); //当影响函数为1,表面注册成功
    }

    /**
     * 2022年6月29日16:44:15
     * 用户登录
     * @param user
     * @return
     */
    @RequestMapping(value = "/userLogin",produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String userLogin(User user, HttpServletResponse response, HttpSession httpSession) throws JsonProcessingException {
        logger.info("登录用户名是:" + user.getUsername());
        logger.info("登录密码是:" + user.getPassword());
        User loginResult = userService.findUserByUser(user);
        boolean isExist = loginResult != null;
        //如果用户存在,保存到域中
        if (isExist){
            httpSession.setAttribute("user",loginResult);
        }
        return Boolean.toString(isExist); //true代表用户存在, false 代表不
    }

    /**
     *2022年7月1日15:37:29
     * @param httpSession 用于获取保存在域中的数据
     * @return 返回当前登录用户
     */
    @RequestMapping("/getCurrentUser")
    @ResponseBody
    public User getCurrentUser(HttpSession httpSession){
        return (User) httpSession.getAttribute("user");
    }
}
