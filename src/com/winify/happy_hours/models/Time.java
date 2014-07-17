package com.winify.happy_hours.models;

import java.io.Serializable;


public class Time implements Serializable {

private int time;

    public Time() {

    }

    public Time(int time ) {
        this.time=time;

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


}
