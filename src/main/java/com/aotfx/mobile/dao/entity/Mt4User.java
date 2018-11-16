package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableName;


public class Mt4User {
    private String user;
    private String broker;
    private String password;
    private int status;
    private String telphone;

    public Mt4User(String user, String broker, String password, int status, String telphone) {
        this.user = user;
        this.broker = broker;
        this.password = password;
        this.status = status;
        this.telphone = telphone;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
