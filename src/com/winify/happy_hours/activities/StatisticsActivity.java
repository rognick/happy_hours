package com.winify.happy_hours.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.controller.TrackerController;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StatisticsActivity extends Activity implements ServiceListener, View.OnClickListener {
    private TextView monthly;
    private Button monthlyBtn;
    private TextView weekly;
    private Button weeklyBtn;
    private TextView daily;
    private Button dailyBtn;
    private int monthlyMiliSec;
    private int dailyMiliSec;
    private int weeklyMiliSec;
    private TrackerController trackerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.statistics);
        super.onCreate(savedInstanceState);

        ServiceGateway serviceGateway = new ServiceGateway(StatisticsActivity.this);
        trackerController = serviceGateway.getTrackerController(this);
        monthly = (TextView) findViewById(R.id.monthly_work);
        monthlyBtn = (Button) findViewById(R.id.monthlyBtn);
        monthlyBtn.setOnClickListener(this);
        weekly = (TextView) findViewById(R.id.weekly_work);
        weeklyBtn = (Button) findViewById(R.id.weeklyBtn);
        weeklyBtn.setOnClickListener(this);
        daily = (TextView) findViewById(R.id.daily_work);
        dailyBtn = (Button) findViewById(R.id.dailyBtn);
        dailyBtn.setOnClickListener(this);
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
        dailyMiliSec = Integer.parseInt(user.getDaily());
        daily.setText(convertTime(user.getDaily()));

        weekly.setText(convertTime(user.getWeekly()));
        weeklyMiliSec = Integer.parseInt(user.getWeekly());

        monthly.setText(convertTime(user.getMonthly()));
        monthlyMiliSec = Integer.parseInt(user.getMonthly());
    }

    private void getStatistics() {
        ApplicationPreferencesActivity preferences = new ApplicationPreferencesActivity(StatisticsActivity.this);
        User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
        trackerController.getWorkedTime(user);
    }

    private String convertTime(String time) {

        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60));
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.dailyBtn: {
            }
            break;
            case R.id.weeklyBtn: {
            }
            break;
            case R.id.monthlyBtn: {
            }
            break;
        }

    }
}
