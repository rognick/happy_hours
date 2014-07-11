package com.winify.happy_hours.activities.service;

import android.app.Activity;
import android.widget.EditText;
import com.winify.happy_hours.R;

import java.util.concurrent.Semaphore;

/**
 * Created by nicolaerogojan on 7/8/14.
 */
public class TimerStartStop extends Thread {

    private EditText timerView;
    private Activity activity;
    private Boolean runThread;

    public TimerStartStop() {

    }

    public TimerStartStop(EditText timerView, Activity activity, Boolean runThread) {
        this.timerView = timerView;
        this.activity = activity;
        this.runThread = runThread;
    }

    public Boolean getRunThread() {
        return runThread;
    }

    public void setRunThread(Boolean runThread) {
        this.runThread = runThread;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public EditText getTimerView() {
        return timerView;
    }

    public void setTimerView(EditText timerView) {
        this.timerView = timerView;
    }


    @Override
    public void run() {
        super.run();

        try {
            synchronized (this) {

                final android.text.format.Time time = new android.text.format.Time();
                final EditText timerView = (EditText) activity.findViewById(R.id.timerView);
                while (runThread) {
                    wait(1000);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            time.setToNow();
                            timerView.setText(time.hour + ":" + time.minute + ":" + time.second);
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
