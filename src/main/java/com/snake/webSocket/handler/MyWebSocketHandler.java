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
import java.util.*;

public class MyWebSocketHandler extends TextWebSocketHandler {
    //记录日志
    private final Logger logger = Logger.getLogger(MyWebSocketHandler.class);
    // 保存所有的用户session uid对应回话
    private final Map<Integer, WebSocketSession> sessions = new HashMap<>();
    // 存储所有用户
    private final Map<Integer, User> Users = new HashMap<>();
    //注入 消息服务
    @Autowired
    private MessageService messageService;

    /**
     * 新用户连接时调用的方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("用户登录!");
        // 获取httpsession
        Map<String, Object> attributes = session.getAttributes();
        HttpSession httpSession = (HttpSession) attributes.get("currHttpSession");
        //获取登录用户信息
        User user = (User) httpSession.getAttribute("user");
        logger.info("当前用户uid:" + user.getUid());
        //给所有已经在线用户发送上线人员的uid
        Set<Integer> allOnlineUid = sessions.keySet();
        for (Integer uid : allOnlineUid) {
            WebSocketSession webSocketSession = sessions.get(uid);
            //发送上线人员id 消息格式 {"msgType:3","onlineUid":uid}
            webSocketSession.sendMessage(new TextMessage("{\"msgType\":3,\"onlineUid\":" + user.getUid() + "}"));
        }
        //将登录用户的uid作为键,存放到sessions集合
        sessions.put(user.getUid(), session);
        //将session的id作为键,存放到User集合
        Users.put(Integer.parseInt(session.getId()), user);
        //发送连接成功确认消息
        session.sendMessage(new TextMessage("{\"msgType\":6}"));
    }

    /**
     * 接受到消息
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("接收到消息:" + message.getPayload());
        //json转换器
        ObjectMapper objectMapper = new ObjectMapper();
        //将数据转换为JSON对象
        MinMessage msg = objectMapper.readValue((String) message.getPayload(), MinMessage.class);
        //判断消息类型
        switch (msg.getMsgType()) {
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
                    logger.info("发送给" + message1.getReceiverId());
                    WebSocketSession ReceiverSession = sessions.get(message1.getReceiverId());
                    //如果用户在线才给对方发送消息
                    if (ReceiverSession != null) {
                        //封装数据 消息类型 1,上线者id
                        logger.info(JSON.toJsonString(message1));
                        TextMessage textMessage = new TextMessage(JSON.toJsonString(message1));
                        //给对方发送消息
                        ReceiverSession.sendMessage(textMessage);
                    }
                    //给发送者发送远消息,消息类型改为2
                    WebSocketSession SenderSession = sessions.get(message1.getSenderId());
                    message1.setMsgType(2);
                    TextMessage textMessage2 = new TextMessage(JSON.toJsonString(message1));
                    SenderSession.sendMessage(textMessage2);
                } else {
                    logger.info("保存数据失败!");
                }
                break;
            case 2: //用户获取所有上线人员的请求
                logger.info("1会话号为:" + session.getId());
                logger.info("2会话号为:" + sessions.get(msg.getSenderId()).getId());
                //获取登录用户的会哈
                User user = Users.get(Integer.parseInt(session.getId()));
                List<User> friends = user.getFriends();
                //用户是否上线数组
                List<Boolean> lineStatus = new ArrayList<Boolean>();
                //判断是否上线, 如果朋友uid在sessions中,就向数组中追加true,否则false
                for (User friend : friends) {
                    if (sessions.get(friend.getUid()) != null) {
                        lineStatus.add(true);
                    } else lineStatus.add(false);
                }
                logger.info("{\"msgType\":5,\"lineStatus\":" + JSON.toJsonString(lineStatus) + "}");
                //发送消息
                session.sendMessage(new TextMessage("{\"msgType\":5,\"lineStatus\":" + JSON.toJsonString(lineStatus) + "}"));
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
        //获取下线用户信息
        String id = session.getId();
        User user = Users.get(Integer.parseInt(id));
        //删除下线用户的session
        sessions.remove(user.getUid());
        //给所有用户发送用户离线信息
        Set<Integer> allOnlineUid = sessions.keySet();
        for (Integer uid : allOnlineUid) {
            WebSocketSession webSocketSession = sessions.get(uid);
            //发送下线人员id 消息格式 {"msgType:4","offlineUid":uid}
            webSocketSession.sendMessage(new TextMessage("{\"msgType\":4,\"offlineUid\":" + user.getUid() + "}"));
        }
        //删除该用户的User
        Users.remove(Integer.parseInt(id));
        super.afterConnectionClosed(session, status);
    }

}
