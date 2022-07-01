package com.snake.dao;

import com.snake.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 2022年6月29日11:12:27
     * 动态Sql
     * @param user
     * @return
     */
    User findUserByUser(User user);

    /**
     * 2022年6月29日16:04:36
     * 插入数据
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 查询用户的好友的信息
     * @param id
     * @return
     */
    List<User> findUserFriendsByUid(int id);
}
