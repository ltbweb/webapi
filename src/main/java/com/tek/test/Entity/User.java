package com.tek.test.Entity;


import com.tek.test.util.DateUtils;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.Date;


public class User {
    String username;
    boolean status;
    String time;
    String ip;

    public User(String username,  String time, String ip,boolean status) {
        this.username = username;
        this.status = status;
        this.time = time;
        this.ip = ip;
    }

    public User() {
       }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return username + "," + time+","+ip+","+status ;
    }


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
