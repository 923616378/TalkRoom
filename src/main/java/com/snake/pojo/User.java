package com.snake.pojo;

import java.util.List;

public class User {
    private int uid;
    private String username;
    private String password;
    private List<User> friends;

    public User() {
    }

    public User(int uid, String username, String password, List<User> friends) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.friends = friends;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", friends=" + friends +
                '}';
    }
}
