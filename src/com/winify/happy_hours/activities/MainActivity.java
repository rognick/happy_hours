package com.winify.happy_hours.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private ApplicationPreferences preferences;
    private Button button;
    private ProgressBar progressBar;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        preferences = new ApplicationPreferences(this);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (!isNetworkAvailable()) {
            AlertDialog ad = new AlertDialog.Builder(MainActivity.this).create();
            ad.setCancelable(false); // This blocks the 'BACK' button
            ad.setMessage("Check you're Internet connection,it might be closed");
            ad.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    System.exit(0);
                }
            });
            ad.show();
        }

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
                    User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
                    trackerController.startWorkTime(user);
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
                } else if (button.getText().equals("Happy Stop")) {
                    User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
                    trackerController.stopWorkTime(user);
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                }
                break;
        }
    }

    @Override
    public void onSuccess(Response response) {
        Toast.makeText(MainActivity.this, "Server OK", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        if (button.getText().equals("Happy Start")) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText("Happy Stop");
            timerStartStop.setRunThread(true);
            thread = new Thread(timerStartStop);
            thread.start();
            preferences.savePreferences(Extra.KEY_TIMER, true);
        } else if (button.getText().equals("Happy Stop")) {
            button.setBackgroundResource(R.drawable.button_start_bg);
            button.setText("Happy Start");
            timerStartStop.setRunThread(false);
            preferences.updatePreferences(Extra.KEY_TIMER, false);
        }
    }

    @Override
    public void onServerFail(RetrofitError error) {
        Toast.makeText(MainActivity.this, "Server Fail", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
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
            case R.id.logout: {


                User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
                trackerController.stopWorkTime(user);
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                trackerController.logOut(user);
                preferences.removePreferences(Extra.KEY_TOKEN);
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
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
        if (prefs.getBoolean(Extra.Notification_Status,false)) {
            startService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    public void stopService() {
        if (prefs.getBoolean(Extra.Notification_Status,false)) {
            stopService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
