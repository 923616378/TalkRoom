package com.snake.service;

import com.snake.pojo.Message;

public interface MessageService {
    /**
     * 2022年7月2日20:11:32
     * @param message 带保存到数据库的数据
     * @return 返回影响行数
     */
    int saveSendMessage(Message message);

    /**
     * 2022年7月3日16:16:26
     * @param uid 用户登录id
     * @param friendId 待查询朋友的id
     * @return 返回两个人之间所有了聊天信息,以数组形式返回
     */
    Message[] findAllMessagesByUidAndFriendId(int uid, int friendId);
}
