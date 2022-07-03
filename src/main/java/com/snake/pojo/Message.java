package com.snake.pojo;

import java.util.Date;

public class Message extends MinMessage{
    private int mid; //消息id
    private Date  sendTime; //具体发送时间

    public Message(MinMessage minMessage) {
        super(minMessage.senderId, minMessage.msgType,minMessage. message, minMessage.receiverId);
        this.mid = -1; //由数据库生成
        this.sendTime = new Date(); //设置为现在时间
    }

    public Message(int senderId, int msgType, String message, int receiverId) {
        super(senderId, msgType, message, receiverId);
        this.mid = -1; //由数据库生成
        this.sendTime = new Date(); //设置为现在时间
    }
    public Message(int mid,  int msgType,int senderId, int receiverId, String message,Date sendTime) {
        super(senderId, msgType, message, receiverId);
        this.mid = mid; //由数据库生成
        this.sendTime = sendTime; //设置为现在时间
    }
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", sendTime=" + sendTime +
                ", senderId=" + senderId +
                ", msgType=" + msgType +
                ", message='" + message + '\'' +
                ", receiverId=" + receiverId +
                '}';
    }
}
