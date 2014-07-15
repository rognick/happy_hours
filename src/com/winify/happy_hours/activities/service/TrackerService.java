package com.winify.happy_hours.activities.service;


import com.winify.happy_hours.activities.models.User;
import retrofit.Callback;
import retrofit.client.Response;

import retrofit.http.*;

/**
 * Created by Mindshifter on 7/2/2014.
 */
public interface TrackerService  {






//todo de modificat path dupa ce termina serverul cu serviciile



    @GET("/start/1")
    void  start( Callback<Response> callback);


    @GET("/start/1")
    void  loginUser(@Path("login") String login,@Path("password") String password, Callback<Response> callback);


    @GET("/stop/1")
    void  logoutUser(@Path("login") String login,@Path("password") String password, Callback<Response> callback);







    @PUT("/update")
    void  updateUser(@Body User user, Callback<Response> callback);



}
