package com.winify.happy_hours.service;


import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.User;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;


public interface TrackerService {


    @GET("/start/1")
    void start(Callback<Response> callback);


    @GET("/start/1")
    void loginUser(@Path("login") String login, @Path("password") String password, Callback<Response> callback);


    @GET("/stop/1")
    void logoutUser(@Path("login") String login, @Path("password") String password, Callback<Response> callback);

    @GET("/time")
    void getTime(Callback<Time> callback);

    @GET("/users/{email}")
    void getUser(@Path("email") String email, Callback<User> callback);


}
