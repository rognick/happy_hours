package com.winify.happy_hours.activities.models;

import java.io.Serializable;


public class User implements Serializable {

    private String login;
    private String password;
    private int id;


    public User() {
        
    }

    public User(String login,String password,int id) {
        this.login = login;
        this.password = password;
        this.id=id;

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(String password) {
        this.id = id;
    }


}
