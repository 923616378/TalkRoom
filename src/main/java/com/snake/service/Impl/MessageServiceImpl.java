package com.snake.service.Impl;

import com.snake.dao.MessageMapper;
import com.snake.pojo.Message;
import com.snake.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    //注入dao层服务
    @Autowired
    private MessageMapper messageMapper;
    /**
     * 2022年7月2日20:12:59
     * @param message 带保存到数据库的数据
     * @return 返回影响行数
     */
    @Override
    public int saveSendMessage(Message message) {
        return messageMapper.insertMessage(message);
    }
    /**
     * 2022年7月3日16:16:53
     * @param uid 用户登录id
     * @param friendId 待查询朋友的id
     * @return 返回两个人之间所有了聊天信息,以数组形式返回
     */
    @Override
    public Message[] findAllMessagesByUidAndFriendId(int uid, int friendId) {
        return messageMapper.queryAllMessagesByUidAndFriendId(uid,friendId);
    }
}
