package com.winify.happy_hours.activities.controller;

import android.content.Context;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import com.winify.happy_hours.activities.service.TrackerService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by Mindshifter on 7/2/2014.
 */
public class TrackerController {

    private final Context context;
    private final TrackerService service;
    private final ServiceListener serviceListener;

    private TrackerController(Context context, TrackerService service, ServiceListener serviceListener) {
        this.context = context;
        this.service = service;
        this.serviceListener = serviceListener;
    }

    public static TrackerController getInstance(Context context, TrackerService service, ServiceListener serviceListener) {
        return new TrackerController(context, service, serviceListener);
    }




    public void updateUser(User user) {
        service.updateUser(user, new Callback<Response>() {
            @Override
            public void success(Response loginResponse, Response response) {
                serviceListener.onSuccess(loginResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }




    public void loginUser(String login, String password){
        service.loginUser(login, password, new Callback<Response>() {


            @Override
            public void success(Response loginresponse, Response response) {
                serviceListener.onSuccess(loginresponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }


    public void logoutUser(String login, String password){
        service.loginUser(login, password, new Callback<Response>() {


            @Override
            public void success(Response logoutresponse, Response response) {
                serviceListener.onSuccess(logoutresponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }




}
