package com.winify.happy_hours.service;

import android.app.Activity;
import android.widget.EditText;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimerStartStop extends Thread {

    private EditText timerView;
    private Activity activity;
    private Boolean runThread;
    private String serverTime;
    private TrackerService service;

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
                service = serviceGateway.getService();
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
        Token token = new Token(preferences.getKeyToken());
        service.getServerTime(token, new ServiceListener<Time>() {
            @Override
            public void success(Time time, Response response) {
                serverTime = convertTime(time.getTime());
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private String convertTime(String time) {
        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60)) % 24;
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + ":" + min;
    }
}
