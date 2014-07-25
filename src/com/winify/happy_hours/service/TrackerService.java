package com.winify.happy_hours.service;


import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.models.User;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;


public interface TrackerService {

    // public static String SERVICE_ENDPOINT = "http://192.168.3.93:9000";
    @POST("/logout")
    void logOut(@Body Token user, Callback<Response> callback);

    @POST("/login")
    void getToken(@Body User user, ServiceListener<Token> callback);

    @POST("/start")
    void startWorkTime(@Body Token token, Callback<Response> callback);

    @POST("/stop")
    void stopWorkTime(@Body Token token, Callback<Response> callback);

    @POST("/time")
    void getServerTime(@Body Token token, ServiceListener<Time> cb);

    @POST("/statistic")
    void getWorkedTime(@Body Token token, ServiceListener<Time> cb);
}
