package com.winify.happy_hours.service;


import com.winify.happy_hours.models.User;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


public interface TrackerService {

  // public static String SERVICE_ENDPOINT = "http://192.168.3.93:9000";

    @POST("/authenticate")
    void getToken(@Body User user, Callback<User> callback);

    @POST("/start")
    void startWorkTime(@Body User user, Callback<Response> callback);

    @POST("/stop")
    void stopWorkTime(@Body User user, Callback<Response> callback);

    @POST("/time")
    void getServerTime(@Body User user,Callback<User> cb);

    @POST("/statistic")
    void getWorkedTime(@Body User user , Callback<User> cb);
}