package com.snake.service.Impl;

import com.snake.controller.UserController;
import com.snake.dao.UserMapper;
import com.snake.pojo.User;
import com.snake.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2022年6月29日10:59:15
 * 用户相关服务处理
 */
@Service
public class UserServiceImpl implements UserService {
    //记录日志
    private final Logger logger = Logger.getLogger(UserServiceImpl.class);
    //注入UserDao
    @Autowired
    private UserMapper userMapper;
    /**
     * 根据User对象里面的信息进行查询数据
     * @param user
     * @return
     */
    @Override
    public User findUserByUser(User user) {
        //动态根据user里面所存在的数据查找用户
        User queryUserResult = userMapper.findUserByUser(user);
        logger.info("查找用户是否存在时返回的数据:"+queryUserResult);
        return queryUserResult;
    }

    /**
     * 根据用户数据进行保存到数据库
     * @param user
     * @return
     */
    @Override
    public int registerByUser(User user) {
        return userMapper.insertUser(user);
    }
}
