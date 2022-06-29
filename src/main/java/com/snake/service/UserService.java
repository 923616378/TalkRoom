package com.snake.service;

import com.snake.pojo.User;

public interface UserService {
    /**
     * 根据用户信息查找用户
     * @return
     */
    User findUserByUser(User user);

    /**
     * 注册
     * @param user
     * @return
     */
    int registerByUser(User user);
}
