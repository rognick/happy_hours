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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Constants;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.service.TrackerService;
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

    private ApplicationPreferences preferences;
    private Button button;
    private ProgressBar progressBar;
    private TrackerService service;
    private String errorMsg;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        preferences = new ApplicationPreferences(this);

        if (preferences.getKeyToken().equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_token_message), Toast.LENGTH_LONG).show();
            redirectLoginPage();
        }
        button = (Button) findViewById(R.id.buttonHappyStart);
        button.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.timerView);
        ServiceGateway serviceGateway = new ServiceGateway(MainActivity.this);
        service = serviceGateway.getService();
        if (preferences.isTimerSet()) {
            button.setBackgroundResource(R.drawable.button_stop_bg);
            button.setText(getResources().getString(R.string.clicked_stop));
        }
        if (!Utils.isNetworkAvailable(this)) {
            AlertDialog ad = new AlertDialog.Builder(MainActivity.this).create();
            ad.setCancelable(false); // This blocks the 'BACK' button
            ad.setMessage(getResources().getString(R.string.server_bad_connection));
            ad.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ad.show();
        }else{  getWorkedTime();}
    }

    private void redirectLoginPage() {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.buttonHappyStart:
                if (button.getText().equals(getResources().getString(R.string.clicked_start))) {
                    Token token = new Token(preferences.getKeyToken());
                    service.startWorkTime(token, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            showSuccessMessage();
                            button.setBackgroundResource(R.drawable.button_stop_bg);
                            button.setText(getResources().getString(R.string.clicked_stop));
                            preferences.setTimer(true);
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            if (retrofitError.getResponse() != null) {
                                if (getErrorMessage(retrofitError).equals("TimerOn")) {
                                    button.setBackgroundResource(R.drawable.button_stop_bg);
                                    button.setText(getResources().getString(R.string.clicked_stop));
                                    preferences.setTimer(true);
                                    showSuccessMessage();
                                } else if (getErrorMessage(retrofitError).equals(Constants.TOKEN_EXPIRE)) {
                                    preferences.removeToken();
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_token_message), Toast.LENGTH_LONG).show();
                                    redirectLoginPage();
                                    finish();
                                } else {
                                    showErrorToast();
                                }
                            } else {
                                showErrorMessage(getResources().getString(R.string.server_bad_connection));
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);

                } else if (button.getText().equals(getResources().getString(R.string.clicked_stop))) {
                    Token token = new Token(preferences.getKeyToken());
                    service.stopWorkTime(token, new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            showSuccessMessage();
                            button.setBackgroundResource(R.drawable.button_start_bg);
                            button.setText(getResources().getString(R.string.clicked_start));
                            preferences.setTimer(false);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            if (getErrorMessage(retrofitError).equals("TimerOff")) {
                                button.setBackgroundResource(R.drawable.button_start_bg);
                                button.setText(getResources().getString(R.string.clicked_start));
                                preferences.setTimer(false);
                                showSuccessMessage();
                            } else if (getErrorMessage(retrofitError).equals(Constants.TOKEN_EXPIRE)) {
                                preferences.removeToken();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_token_message), Toast.LENGTH_LONG).show();
                                redirectLoginPage();
                            } else {
                                showErrorToast();
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

    private void showErrorToast() {
        Toast.makeText(MainActivity.this, "Server connection failed", Toast.LENGTH_SHORT).show();
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
            case R.id.refresh_data: {
                getWorkedTime();
            }
            break;
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
                        if (button.getText().equals(getResources().getString(R.string.clicked_stop))) {
                            button.setBackgroundResource(R.drawable.button_start_bg);
                            button.setText(getResources().getString(R.string.clicked_start));
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

    private void showErrorMessage(String error) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setMessage(error);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void getWorkedTime() {
        Token token = new Token(preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {
            @Override
            public void success(Time time, Response response) {
                textView.setText(convertTime(time.getDaily()));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (retrofitError.getResponse() != null) {
                    if (getErrorMessage(retrofitError).equals(Constants.TOKEN_EXPIRE)) {
                        preferences.removeToken();
                        showErrorMessage("Your session has expired, please logout in login again");
                    }
                } else {
                    showErrorMessage(getResources().getString(R.string.server_bad_connection));
                }
            }
        });
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

    private String convertTime(String time) {
        int milliseconds = Integer.parseInt(time);
        int hour = (milliseconds / (1000 * 60 * 60)) % 24;
        int min = ((milliseconds - (milliseconds / (1000 * 60 * 60))) / (1000 * 60)) % 60;
        return hour + "h : " + min + "m";
    }
}
