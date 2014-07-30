package com.winify.happy_hours.models;


import java.io.Serializable;

public class Time implements Serializable {

    private String daily;
    private String monthly;
    private String weekly;
    private String WorkedDays;
    private String TimeToWork;

    public Time() {
    }

    public Time(String daily, String weekly, String monthly,String WorkedDays,String TimeToWork) {

        this.daily = daily;
        this.monthly = monthly;
        this.weekly = weekly;
        this.WorkedDays=WorkedDays;
        this.TimeToWork=TimeToWork;
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

    public String getWorkedDays() {
        return WorkedDays;
    }

    public void setWorkedDays(String WorkedDays) {
        this.WorkedDays = WorkedDays;
    }

    public String getTimeToWork() {
        return TimeToWork;
    }

    public void setTimeToWork(String TimeToWork) {
        this.TimeToWork = TimeToWork;
    }

}
