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

    public Thread thread = new Thread();
    public TimerStartStop timerStartStop = null;
    public EditText editText;
    private TrackerController trackerController;
    private ApplicationPreferencesActivity preferences;
    private  Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        preferences = new ApplicationPreferencesActivity(this);

        if (preferences.getKeyToken().equals("")) {
            Toast.makeText(getApplicationContext(), "Token expired", Toast.LENGTH_LONG).show();
            redirectLoginPage();
        }
        stopService();

         button = (Button) findViewById(R.id.buttonHappyStart);
        button.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.timerView);
        timerStartStop = new TimerStartStop(editText, this, true);

        if (preferences.getKeyTimerStatus()) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText("Happy Stop");
            thread = new Thread(timerStartStop);
            thread.start();
        }

        ServiceGateway serviceGateway = new ServiceGateway(MainActivity.this);
        trackerController = serviceGateway.getTrackerController(this);

    }

    private void redirectLoginPage() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick(View click) {

        switch (click.getId()) {
            case R.id.buttonHappyStart:
                if (button.getText().equals("Happy Start")) {
                    User user = new User("", "",preferences.getKeyToken(),"","","","");
                    trackerController.startWorkTime(user);





                } else if (button.getText().equals("Happy Stop")) {

                    User user = new User("", "",preferences.getKeyToken(),"","","","");
                    trackerController.stopWorkTime(user);

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onSuccess(Response response) {
        Toast.makeText(MainActivity.this, "Server OK", Toast.LENGTH_SHORT).show();

        if (button.getText().equals("Happy Start")) { button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText("Happy Stop");
            timerStartStop.setRunThread(true);
            thread = new Thread(timerStartStop);
            thread.start();
            preferences.savePreferences(Extra.KEY_TIMER, true);
        }
        else if (button.getText().equals("Happy Stop")) {  button.setBackgroundResource(R.drawable.button_start_bg);
            button.setText("Happy Start");
            timerStartStop.setRunThread(false);
            preferences.updatePreferences(Extra.KEY_TIMER, false);}


    }

    @Override
    public void onServerFail(RetrofitError error) {
        Toast.makeText(MainActivity.this, "Server Fail", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsersList(User user) {

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

                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
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
        if (preferences.getNotificationStatus()) {
            startService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    public void stopService() {
        if (preferences.getNotificationStatus()) {
            stopService(new Intent(getBaseContext(), WifiService.class));
        }
    }
}
