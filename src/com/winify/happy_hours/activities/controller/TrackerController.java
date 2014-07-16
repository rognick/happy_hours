package com.winify.happy_hours.activities.controller;

import android.content.Context;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.Time;
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


    public void start(){
        service.start(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

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


    public void getTime(){
        service.getTime(new Callback<Time>() {
            @Override
            public void success(Time time, Response response) {
                serviceListener.recieveTime(time);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }




}
