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





    @GET("/users/login/{login}/{password}")
    void  loginUser(@Path("login") String login,@Path("password") String password, Callback<Response> callback);


    @GET("/users/logout/{login}/{password}")
    void  logoutUser(@Path("login") String login,@Path("password") String password, Callback<Response> callback);







    @PUT("/update")
    void  updateUser(@Body User user, Callback<Response> callback);



}
