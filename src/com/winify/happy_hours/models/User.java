package com.winify.happy_hours.models;

import java.io.Serializable;


public class User implements Serializable {

    private String username;
    private String password;
    private String token;
    private String daily;
    private String monthly;
    private String weekly;
    private String time;

    public User() {

    }


    public User(String username, String password, String token, String daily, String weekly, String monthly, String time) {

        this.username = username;
        this.password = password;
        this.token = token;
        this.daily = daily;
        this.monthly = monthly;
        this.weekly = weekly;
        this.time = time;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public String getWeekly() {
        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
