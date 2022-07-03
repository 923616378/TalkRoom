package com.snake.controller;

import com.snake.pojo.Message;
import com.snake.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message")
public class MessageController {
        //记录日志
        private final Logger logger = Logger.getLogger(MessageController.class);
        //注入MessageService
        @Autowired
        private MessageService messageService;
    /**
     * 2022年7月3日16:12:51
     * @param uid 用户登录id
     * @param friendId 待查询朋友的id
     * @return 返回两个人之间所有了聊天信息,以数组形式返回
     */
    @RequestMapping("/getAllMessagesByUidAndFriendId")
    @ResponseBody
    public Message[] getAllMessagesByUidAndFriendId(int uid,int friendId){
        return messageService.findAllMessagesByUidAndFriendId(uid,friendId);
    }
}
