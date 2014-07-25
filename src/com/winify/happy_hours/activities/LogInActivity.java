package com.winify.happy_hours.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.TrackerService;
import com.winify.happy_hours.utils.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInActivity extends Activity {
    private EditText login;
    private EditText password;
    private ApplicationPreferences preferences;
    private ProgressBar progressBar;
    private SharedPreferences prefs;
    private TrackerService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        ServiceGateway serviceGateway = new ServiceGateway(LogInActivity.this);
        service = serviceGateway.getService();

        if (!Utils.isNetworkAvailable(this)) {
            AlertDialog ad = new AlertDialog.Builder(LogInActivity.this).create();
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

        preferences = new ApplicationPreferences(this);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefs.getString(Extra.KEY_IP, "Default NickName").equals("") ||
                        prefs.getString(Extra.KEY_PORT, "Default NickName").equals("")) {

                    AlertDialog ad = new AlertDialog.Builder(LogInActivity.this).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button
                    ad.setMessage("Check youre Ip Address and Port in  settings");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                } else {
                    if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                        getKeyToken(login.getText().toString(), password.getText().toString());
                        progressBar = (ProgressBar) findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.VISIBLE);
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
        preferences.removePreferences(Extra.KEY_TOKEN);
        Log.d("Tag","GET TOKEN");
        service.getToken(user, new ServiceListener<Token>() {

            @Override
            public void success(Token token, Response response) {
                preferences.savePreferences(Extra.KEY_TOKEN, token.getToken());
                Toast.makeText(LogInActivity.this, preferences.getKeyToken(), Toast.LENGTH_LONG).show();
                redirectHomePage();
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                progressBar.setVisibility(View.GONE);
                AlertDialog ad = new AlertDialog.Builder(LogInActivity.this).create();
                ad.setCancelable(false); // This blocks the 'BACK' button
                ad.setMessage("Please check youre Username and Password or youre connection with Server");
                ad.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
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
}

