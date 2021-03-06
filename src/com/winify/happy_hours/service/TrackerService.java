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
    @POST("/logout")
    void logOut(@Body Token user, Callback<Response> callback);

    @POST("/login")
    void getToken(@Body User user, ServiceListener<Token> callback);

    @POST("/start")
    void startWorkTime(@Body Token token, Callback<Response> callback);

    @POST("/stop")
    void stopWorkTime(@Body Token token, Callback<Response> callback);

    @POST("/statistic")
    void getWorkedTime(@Body Token token, ServiceListener<Time> cb);
}
