package com.winify.happy_hours.activities.controller;

import android.content.Context;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import com.winify.happy_hours.activities.service.TrackerService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public void getUser(String login, String password) {
        service.getUser(login, password, new Callback<User>() {


            @Override
            public void success(User user, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });


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





}
