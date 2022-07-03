package com.snake.dao;

import com.snake.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 2022年7月3日16:22:41
     * @param uid 用户
     * @param friendId 用户朋友id
     * @return 所有聊天消息
     */
    @Select("SELECT mid,msgType,senderId,receiverId,message,sendTime  FROM Message_tb " +
            "WHERE ((senderId =#{uid} and receiverId = #{friendId}) OR (senderId =#{friendId} and receiverId = #{uid}))")
    Message[] queryAllMessagesByUidAndFriendId(@Param("uid") int uid,@Param("friendId") int friendId);
}
