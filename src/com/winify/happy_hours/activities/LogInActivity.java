package com.winify.happy_hours.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TrackerService;
import com.winify.happy_hours.utils.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditText login;
    private EditText password;
    private ApplicationPreferences preferences;
    private ProgressBar progressBar;
    private TrackerService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ServiceGateway serviceGateway = new ServiceGateway(LogInActivity.this);
        service = serviceGateway.getService();
        preferences = new ApplicationPreferences(this);

        if (!Utils.isNetworkAvailable(this)) {
            showErrorMessage(getResources().getString(R.string.bad_network_connection));
        }

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preferences.getIp().equals("") ||
                        preferences.getPort().equals("")) {
                    showErrorIpMessage("Check your Ip Address and Port in  settings");
                } else {
                    if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                        getKeyToken(login.getText().toString(), password.getText().toString());
                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        showErrorMessage(getResources().getString(R.string.empty_login_password));
                    }
                }
            }
        });
    }

    private void redirectHomePage() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        LogInActivity.this.finish();
    }

    public void getKeyToken(String login, String password) {
        User user = new User(login, password);
        preferences.removeToken();
        service.getToken(user, new ServiceListener<Token>() {

            @Override
            public void success(Token token, Response response) {
                preferences.saveToken(token.getToken());
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.welcome_message), Toast.LENGTH_LONG).show();
                redirectHomePage();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (retrofitError.getResponse() != null) {
                    showErrorMessage("Please check your Username and Password");
                } else if (!Utils.isNetworkAvailable(LogInActivity.this)) {
                    showErrorMessage(getResources().getString(R.string.bad_network_connection));
                } else {

                    showErrorMessage(getResources().getString(R.string.server_bad_connection));
                }
                progressBar.setVisibility(View.GONE);
            }
        });
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

    private void showErrorIpMessage(String error) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setMessage(error);
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LogInActivity.this, SettingsActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        ad.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.loginmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.startSettings: {
                Intent intent = new Intent(LogInActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings;
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        recreate();
    }
}