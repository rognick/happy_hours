package com.winify.happy_hours.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.constants.Extra;
import com.winify.happy_hours.activities.controller.Prefs;
import com.winify.happy_hours.activities.controller.ServiceGateway;
import com.winify.happy_hours.activities.controller.TrackerController;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import com.winify.happy_hours.activities.service.CalendarActivity;
import com.winify.happy_hours.activities.service.SettingsActivity;
import com.winify.happy_hours.activities.service.TimerStartStop;
import com.winify.happy_hours.activities.service.WifiService;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class MainActivity extends Prefs implements ServiceListener, View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    public static final String PREFS_NAME = "LoginPrefs";

    private TrackerController trackerController;
    public Thread thread = new Thread();
    public TimerStartStop timerStartStop = null;
    public Boolean connectet = true;
    public EditText editText;


    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        stopService();


        Button button = (Button) findViewById(R.id.buttonHappyStart);
        button.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.timerView);
        timerStartStop = new TimerStartStop(editText, this, true);


//        If login to server
        if (settings.getString("timer", "").equals("true")) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText("Happy Stop");
            thread = new Thread(timerStartStop);
            thread.start();
        }

        sayHelloUser();

        ServiceGateway serviceGateway = new ServiceGateway(MainActivity.this);
        trackerController = serviceGateway.getTrackerController(this);


    }

    public void sayHelloUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String value1 = settings.getString("login", "");
        String value2 = settings.getString("password", "");

        Toast.makeText(getApplicationContext(), "Salut " + value1,
                Toast.LENGTH_LONG).show();
    }


    public void onClick(View click) {

        switch (click.getId()) {
            case R.id.buttonHappyStart:

                Button button = (Button) findViewById(R.id.buttonHappyStart);

                if (button.getText().equals("Happy Start")) {
                    button.setBackgroundResource(R.drawable.button_stop_bg);
                    button.setText("Happy Stop");
                    timerStartStop.setRunThread(true);
                    thread = new Thread(timerStartStop);
                    thread.start();
                    savePrefs("timer", "true");
                    trackerController.loginUser(settings.getString("login", "").toString(), settings.getString("password", "").toString());


                } else if (button.getText().equals("Happy Stop")) {
                    button.setBackgroundResource(R.drawable.button_start_bg);
                    button.setText("Happy Start");
                    timerStartStop.setRunThread(false);
                    updatePrefs("timer", "false");
                    trackerController.logoutUser(settings.getString("login", "").toString(), settings.getString("password", "").toString());


                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onSuccess(Response response) {
        Toast.makeText(MainActivity.this, "Server OK", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServerFail(RetrofitError error) {
        Toast.makeText(MainActivity.this, "Server Fail", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUsersList(List<User> list) {
        Toast.makeText(MainActivity.this, "Users list ok", Toast.LENGTH_SHORT).show();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.settings: {

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);


                startActivity(intent);


            }
            break;


            case R.id.statistic: {


                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);


                startActivity(intent);


            }
            break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {

// Thread stop
        timerStartStop.setRunThread(false);
        startService();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
//
        super.onStop();
    }

    // Method to start the service
    public void startService() {
        if (settings.getBoolean(Extra.Notification_Status, false)) {
            startService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    // Method to stop the service
    public void stopService() {
        if (settings.getBoolean(Extra.Notification_Status, false)) {
            stopService(new Intent(getBaseContext(), WifiService.class));
        }
    }


}
