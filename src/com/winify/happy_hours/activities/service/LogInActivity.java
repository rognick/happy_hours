package com.winify.happy_hours.activities.service;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.MainActivity;
import com.winify.happy_hours.activities.controller.Prefs;


/**
 * Created by Cornel on 7/7/2014.
 */
public class LogInActivity extends Prefs {


    private TextView login;
    private TextView password;
    SharedPreferences settings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        /*
         * Check if we successfully logged in before.
         * If we did, redirect to home page
         */
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (settings.getString("login", "").toString().equals("test") &&
                settings.getString("password", "").toString().equals("test")) {
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            LogInActivity.this.finish();

        }

        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login = (EditText) findViewById(R.id.login);
                EditText password = (EditText) findViewById(R.id.password);


                if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    if (login.getText().toString().equals("test") && password.getText().toString().equals("test")) {
                        /*
                         * So login information is correct,
						 * we will save the Preference data
						 * and redirect to next class / home
						 */
                        savePrefs("login",login);
                        savePrefs("password",password);

                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);

                        startActivity(intent);

                        LogInActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentification Failed !!! \nPlease try again",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
