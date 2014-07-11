package com.winify.happy_hours.activities.service;

import com.winify.happy_hours.activities.models.User;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Mindshifter on 7/2/2014.
 */
public interface TrackerService {

    public static String SERVICE_ENDPOINT = "http://192.168.3.82:9000";


//todo de modificat path dupa ce termina serverul cu serviciile
    @POST("/users/{login+password}")
    void getUser(@Path("login+password") String email,String password, Callback<User> callback);

    @PUT("/update")
    void  updateUser(@Body User user, Callback<Response> callback);



}
