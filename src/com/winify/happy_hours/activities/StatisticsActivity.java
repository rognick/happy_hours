package com.winify.happy_hours.activities;

import android.app.Activity;
import android.os.Bundle;
import com.winify.happy_hours.R;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatisticsActivity extends Activity implements ServiceListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.statistics);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onServerFail(RetrofitError error) {

    }

    @Override
    public void onUsersList(User user) {

    }

}
