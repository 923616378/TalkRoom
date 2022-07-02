package com.snake.dao;

import com.snake.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper {
    /**
     * 保存用户发送的消息
     * @param message
     * @return 返回影响行数
     */
    @Insert("Insert Message_tb Values(null,#{msgType},#{senderId},#{receiverId},#{message},#{sendTime})")
    int insertMessage(Message message);
}
