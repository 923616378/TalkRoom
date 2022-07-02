package com.snake.pojo;

public class MinMessage {

    protected int senderId;
    protected int msgType;
    protected String message;
    protected int receiverId;

    public MinMessage() {
    }

    public MinMessage(int senderId, int msgType, String message, int receiverId) {
        this.senderId = senderId;
        this.msgType = msgType;
        this.message = message;
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "MinMessage{" +
                "senderId=" + senderId +
                ", msgType=" + msgType +
                ", message='" + message + '\'' +
                ", receiverId=" + receiverId +
                '}';
    }
}
