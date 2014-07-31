package com.winify.happy_hours.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.service.TimerStartStop;
import com.winify.happy_hours.service.TrackerService;
import com.winify.happy_hours.service.WifiService;
import com.winify.happy_hours.utils.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements View.OnClickListener {

    private Thread thread = new Thread();
    private TimerStartStop timerStartStop = null;
    private ApplicationPreferences preferences;
    private Button button;
    private ProgressBar progressBar;
    private TrackerService service;
    private String errorMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        preferences = new ApplicationPreferences(this);

        if (!Utils.isNetworkAvailable(this)) {
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
            Toast.makeText(getApplicationContext(), Extra.BAD_TOKEN_MESSAGE, Toast.LENGTH_LONG).show();
            redirectLoginPage();
        }
        stopService();
        button = (Button) findViewById(R.id.buttonHappyStart);
        button.setOnClickListener(this);

        EditText editText = (EditText) findViewById(R.id.timerView);
        timerStartStop = new TimerStartStop(editText, this, true);

        if (preferences.isTimerSet()) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText(Extra.CLICKED_STOP);
            thread = new Thread(timerStartStop);
            thread.start();
        }
        ServiceGateway serviceGateway = new ServiceGateway(MainActivity.this);
        service = serviceGateway.getService();
    }

    private void redirectLoginPage() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.buttonHappyStart:
                if (button.getText().equals(Extra.CLICKED_START)) {
                    Token token = new Token(preferences.getKeyToken());
                    service.startWorkTime(token, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            showSuccessMessage();
                            button.setBackgroundResource(R.drawable.button_stop_bg);
                            button.setText(Extra.CLICKED_STOP);
                            timerStartStop.setRunThread(true);
                            thread = new Thread(timerStartStop);
                            thread.start();
                            preferences.setTimer(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {

                            if (getErrorMessage(retrofitError).equals("TimerOn")) {
                                Token token = new Token(preferences.getKeyToken());
                                service.stopWorkTime(token, new Callback<Response>() {

                                    @Override
                                    public void success(Response response, Response response2) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        showErrorMessage();

                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                                showNotificationMessage();
                            } else if (getErrorMessage(retrofitError).equals(Extra.TOKEN_EXPIRE)) {
                                preferences.removeToken();
                                Toast.makeText(getApplicationContext(), Extra.BAD_TOKEN_MESSAGE, Toast.LENGTH_LONG).show();
                                redirectLoginPage();
                                finish();
                            } else {
                                showErrorMessage();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                } else if (button.getText().equals(Extra.CLICKED_STOP)) {
                    Token token = new Token(preferences.getKeyToken());
                    service.stopWorkTime(token, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            showSuccessMessage();
                            button.setBackgroundResource(R.drawable.button_start_bg);
                            button.setText(Extra.CLICKED_START);
                            timerStartStop.setRunThread(false);
                            preferences.setTimer(false);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            if (getErrorMessage(retrofitError).equals("TimerOff")) {
                                Token token = new Token(preferences.getKeyToken());
                                service.startWorkTime(token, new Callback<Response>() {

                                    @Override
                                    public void success(Response response, Response response2) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        showErrorMessage();

                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                                showNotificationMessage();
                            } else if (getErrorMessage(retrofitError).equals(Extra.TOKEN_EXPIRE)) {
                                preferences.removeToken();
                                Toast.makeText(getApplicationContext(), Extra.BAD_TOKEN_MESSAGE, Toast.LENGTH_LONG).show();
                                redirectLoginPage();
                                finish();
                            } else {
                                showErrorMessage();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private String getErrorMessage(RetrofitError retrofitError) {
        if (retrofitError.getResponse() != null) {
            TypedInput body = retrofitError.getResponse().getBody();
            byte[] bytes = new byte[0];
            try {
                bytes = streamToBytes(body.in());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String charset = MimeUtil.parseCharset(body.mimeType());
            try {
                errorMsg = new String(bytes, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return errorMsg;
    }

    private void showSuccessMessage() {
        Toast.makeText(MainActivity.this, "Server connection succeed", Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage() {
        Toast.makeText(MainActivity.this, "Server connection failed", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    private void showNotificationMessage() {
        Toast.makeText(MainActivity.this, "Try to click again", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
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

                Token token = new Token(preferences.getKeyToken());
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                service.logOut(token, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        if (button.getText().equals(Extra.CLICKED_STOP)) {
                            button.setBackgroundResource(R.drawable.button_start_bg);
                            button.setText(Extra.CLICKED_START);
                            timerStartStop.setRunThread(false);
                            preferences.setTimer(false);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

                preferences.removeToken();
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
        if (preferences.isNotificationStatusSet()) {
            startService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    public void stopService() {
        if (preferences.isNotificationStatusSet()) {
            stopService(new Intent(getBaseContext(), WifiService.class));
        }
    }

    static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[1024];
            int r;
            while ((r = stream.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }
}
