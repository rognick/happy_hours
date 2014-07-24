package com.winify.happy_hours.service;

import android.app.Activity;
import android.widget.EditText;
import com.winify.happy_hours.activities.ApplicationPreferences;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.controller.TrackerController;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimerStartStop extends Thread implements ServiceListener {

    private EditText timerView;
    private Activity activity;
    private Boolean runThread;
    private TrackerController trackerController;
    private String serverTime;

    public TimerStartStop(EditText timerView, Activity activity, Boolean runThread) {
        this.timerView = timerView;
        this.runThread = runThread;
        this.activity = activity;
    }

    public void setRunThread(Boolean runThread) {
        this.runThread = runThread;
    }

    @Override
    public void run() {

        super.run();

        try {
            synchronized (this) {
                ServiceGateway serviceGateway = new ServiceGateway(activity);
                trackerController = serviceGateway.getTrackerController(this);
                while (runThread) {
                    getServerTime();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerView.setText(serverTime);
                        }
                    });

                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getServerTime() {
        ApplicationPreferences preferences = new ApplicationPreferences(activity);
        User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
        trackerController.getServerTime(user);
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onServerFail(RetrofitError error) {
    }

    @Override
    public void onUsersList(User user) {
        serverTime = convertTime(user.getTime());
    }

    private String convertTime(String time) {
        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60)) % 24;
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }
}
