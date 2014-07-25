package com.winify.happy_hours.models;


import java.io.Serializable;

public class Time implements Serializable {

    private String daily;
    private String monthly;
    private String weekly;
    private String time;

    public Time() {
    }

    public Time(String daily, String weekly, String monthly, String time) {

        this.daily = daily;
        this.monthly = monthly;
        this.weekly = weekly;
        this.time = time;
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
