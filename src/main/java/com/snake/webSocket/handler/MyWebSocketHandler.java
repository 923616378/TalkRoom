package com.snake.webSocket.handler;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snake.Utils.JSON;
import com.snake.pojo.Message;
import com.snake.pojo.MinMessage;
import com.snake.pojo.User;
import com.snake.service.MessageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class MyWebSocketHandler extends TextWebSocketHandler {
    //记录日志
    private final Logger logger = Logger.getLogger(MyWebSocketHandler.class);
    // 保存所有的用户session uid对应回话
    private final Map<Integer,WebSocketSession> sessions = new HashMap<>();
    //注入 消息服务
    @Autowired
    private MessageService messageService;
    /**
     * 关闭连接时调用的方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("用户登录!");

        // 获取httpsession
        Map<String, Object> attributes = session.getAttributes();
        HttpSession httpSession = (HttpSession) attributes.get("currHttpSession");
        //获取登录用户信息
        User user = (User)httpSession.getAttribute("user");
        logger.info("当前用户uid:"+user.getUid());
        //将登录用户的uid作为键,存放到sessions集合
        sessions.put(user.getUid(),session);
    }

    /**
     * 接受到消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("接收到消息:"+message.getPayload());
        //json转换器
        ObjectMapper objectMapper = new ObjectMapper();
        //将数据转换为JSON对象
        MinMessage msg =objectMapper.readValue((String)message.getPayload(),MinMessage.class);
        //判断消息类型
        switch(msg.getMsgType())
        {
            //消息类型1 用户发送给用户的信息
            case 1:
                //将minMessage封装为Message类型
                Message message1 = new Message(msg);
                logger.info(message1);
                //保存到数据库
                int effectLine = messageService.saveSendMessage(message1);
                if (effectLine == 1) {
                    logger.info("保存数据成功!");
                    //将里面的数据转发给对应的用户
                    //根据接受者uid,获取session
                    WebSocketSession webSocketSession = sessions.get(message1.getReceiverId());
                    //封装数据 消息类型 1,上线者id
                    logger.info(JSON.toJsonString(message1));
                    TextMessage textMessage = new TextMessage(JSON.toJsonString(message1));
                    //给它发送消息
                    webSocketSession.sendMessage(textMessage);
                } else {
                    logger.info("保存数据失败!");
                }
                break;
        }
        super.handleMessage(session, message);
    }

    /**
     * 连接关闭时调用
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("用户退出!");
        super.afterConnectionClosed(session, status);
    }
}
