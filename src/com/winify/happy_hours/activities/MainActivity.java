package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.controller.TrackerController;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TimerStartStop;
import com.winify.happy_hours.service.WifiService;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity implements ServiceListener, View.OnClickListener {

    private TrackerController trackerController;
    public Thread thread = new Thread();
    public TimerStartStop timerStartStop = null;
    public EditText editText;
    private ApplicationPreferencesActivity preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        preferences= new ApplicationPreferencesActivity(this);
        stopService();

        Button button = (Button) findViewById(R.id.buttonHappyStart);
        button.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.timerView);
        timerStartStop = new TimerStartStop(editText, this, true);

        if (preferences.getBooleanValueFromPreferences(Extra.KEY_TIMER)) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText("Happy Stop");
            thread = new Thread(timerStartStop);
            thread.start();
        }

        ServiceGateway serviceGateway = new ServiceGateway(MainActivity.this);
        trackerController = serviceGateway.getTrackerController(this);
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
                    preferences.savePreferences(Extra.KEY_TIMER, true);
                    trackerController.logoutUser(preferences.getStringValueFromPreferences(Extra.KEY_USER_NAME),
                            preferences.getStringValueFromPreferences(Extra.KEY_PASSWORD));

                } else if (button.getText().equals("Happy Stop")) {
                    button.setBackgroundResource(R.drawable.button_start_bg);
                    button.setText("Happy Start");
                    timerStartStop.setRunThread(false);
                    preferences.updatePreferences(Extra.KEY_TIMER, false);
                    trackerController.logoutUser(preferences.getStringValueFromPreferences(Extra.KEY_USER_NAME),
                            preferences.getStringValueFromPreferences(Extra.KEY_PASSWORD));

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
    public void getUser(User user) {
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

                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);


            }
            break;

            case R.id.statistic: {


                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);


            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        timerStartStop.setRunThread(false);
        startService();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void startService() {
        if (preferences.getBooleanValueFromPreferences(Extra.Notification_Status)) {
            startService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    public void stopService() {
        if (preferences.getBooleanValueFromPreferences(Extra.Notification_Status)) {
            stopService(new Intent(getBaseContext(), WifiService.class));
        }
    }
}
