package com.example.fruitmanagement.models;

import java.io.Serializable;

public class Message implements Serializable {
    private String username;
    private String content;
    private String roomName;

    public Message(String username, String content, String roomName) {
        this.username = username;
        this.content = content;
        this.roomName = roomName;
    }

    public Message(String username, String roomName) {
        this.username = username;
        this.roomName = roomName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
