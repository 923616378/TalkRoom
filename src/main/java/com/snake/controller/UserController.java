package com.snake.controller;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    //记录日志
    Logger logger = Logger.getLogger(UserController.class);
    //业务层数据
    /**
     *  2022年6月28日17:47:26
     *  判断用户输入的用户名是否已经存在  application/json text/html
     * @param username
     * @return
     */
    @RequestMapping(value = "/userIsExist",produces = {"text/html;charset=utf-8"}) //指定返回数据类型
    @ResponseBody //设置返回数据是响应体,直接返回数据本身
    public String userIsExist(String username){
        logger.info("注册用户名是"+username);
        return "123";
    }
}
