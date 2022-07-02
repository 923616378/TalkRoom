package com.snake.service;

import com.snake.pojo.Message;

public interface MessageService {
    /**
     * 2022年7月2日20:11:32
     * @param message 带保存到数据库的数据
     * @return 返回影响行数
     */
    int saveSendMessage(Message message);
}
