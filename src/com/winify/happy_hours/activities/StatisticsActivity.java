package com.winify.happy_hours.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.controller.TrackerController;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatisticsActivity extends Activity implements ServiceListener {
    private TextView monthly;
    private TextView weekly;
    private TextView daily;
    private TrackerController trackerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.statistics);
        super.onCreate(savedInstanceState);

        ServiceGateway serviceGateway = new ServiceGateway(StatisticsActivity.this);
        trackerController = serviceGateway.getTrackerController(this);

        monthly = (TextView) findViewById(R.id.monthly_work);
        weekly = (TextView) findViewById(R.id.weekly_work);
        daily = (TextView) findViewById(R.id.daily_work);

        getStatistics();
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onServerFail(RetrofitError error) {
    }

    @Override
    public void onUsersList(User user) {
        daily.setText(convertTime(user.getDaily()));
        weekly.setText(convertTime(user.getWeekly()));
        monthly.setText(convertTime(user.getMonthly()));

    }

    private void getStatistics() {
        ApplicationPreferencesActivity preferences = new ApplicationPreferencesActivity(StatisticsActivity.this);
        User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
        trackerController.getWorkedTime(user);
    }

    private String convertTime(String time) {

        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60)) ;
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }
}
