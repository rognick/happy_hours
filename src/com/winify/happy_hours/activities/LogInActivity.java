package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;

public class LogInActivity extends Activity {
    private EditText login;
    private EditText password;
    private ApplicationPreferencesActivity preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);


        preferences = new ApplicationPreferencesActivity(this);


        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        boolean isAlreadyLoggedIn = (preferences.getStringValueFromPreferences(Extra.KEY_USER_NAME).equals("test")
                && preferences.getStringValueFromPreferences(Extra.KEY_USER_NAME).equals("test"));

        Log.d("username",preferences.getStringValueFromPreferences(Extra.KEY_USER_NAME));
        if (isAlreadyLoggedIn) {
            redirectHomePage();
        }

        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {

                    if (login.getText().toString().equals("test") && password.getText().toString().equals("test")) {
                        preferences.saveCredentials(login.getText().toString(), password.getText().toString());


                        redirectHomePage();
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentification Failed !!! \nPlease try again",
                                Toast.LENGTH_LONG).show();
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
}

