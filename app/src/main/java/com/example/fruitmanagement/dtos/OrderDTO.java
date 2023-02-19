package com.example.fruitmanagement.dtos;

import java.io.Serializable;
import java.util.Date;

public class OrderDTO implements Serializable {
    private int id;
    private Date time;
    private String userId;

    public OrderDTO(int id, Date time) {
        this.id = id;
        this.time = time;
    }

    public OrderDTO(Date time, String userId) {
        this.time = time;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
