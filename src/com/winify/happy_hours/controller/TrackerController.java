package com.winify.happy_hours.controller;

import android.content.Context;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TrackerService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

    public void logOut(User user) {
        service.logOut(user, new Callback<Response>() {
            @Override
            public void success(Response logoutResponse, Response response) {
                serviceListener.onSuccess(logoutResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }


    public void geToken(User user) {
        service.getToken(user, new Callback<User>() {

            @Override
            public void success(User user, Response response) {
                serviceListener.onUsersList(user);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }

    public void getServerTime(User user) {
        service.getServerTime(user, new Callback<User>() {

            @Override
            public void success(User user, Response response) {
                serviceListener.onUsersList(user);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }

    public void getWorkedTime(User user) {

        service.getWorkedTime(user, new Callback<User>() {

            @Override
            public void success(User user, Response response) {
                serviceListener.onUsersList(user);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });

    }

    public void startWorkTime(User user) {
        service.startWorkTime(user, new Callback<Response>() {
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

    public void stopWorkTime(User user) {
        service.stopWorkTime(user, new Callback<Response>() {
            @Override
            public void success(Response logoutResponse, Response response) {
                serviceListener.onSuccess(logoutResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                serviceListener.onServerFail(retrofitError);
            }
        });
    }
}
