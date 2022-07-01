package com.snake.service;

import com.snake.pojo.User;

import java.util.List;

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

    /**
     *
     * @param uid 通过用户uid查询朋友
     * @return 返回用户集合
     */
    List<User> findUserFriendsByUid(int uid);
}
