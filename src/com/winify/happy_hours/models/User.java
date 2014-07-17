package com.winify.happy_hours.models;

import java.io.Serializable;


public class User implements Serializable {

    private String email;
    private String firstName;
    private String lastName;
    private int age;

    public User() {
        
    }

    public User(String email, String firstName, String lastName, int age) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public  String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {this.age = age;}

    public int getAge() {return age;}
}
