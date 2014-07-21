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
import com.winify.happy_hours.service.ServiceSettings;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInActivity extends Activity implements ServiceListener {
    private EditText login;
    private EditText password;
    private ApplicationPreferencesActivity preferences;
    private TrackerController trackerController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);

        ServiceGateway serviceGateway = new ServiceGateway(LogInActivity.this);
        trackerController = serviceGateway.getTrackerController(this);

        preferences = new ApplicationPreferencesActivity(this);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        Button b = (Button) findViewById(R.id.logingBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    getKeyToken(login.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    private void redirectHomePage() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
        LogInActivity.this.finish();
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onServerFail(RetrofitError error) {
    }

    @Override
    public void onUsersList(User user) {
        preferences.savePreferences(Extra.KEY_TOKEN, user.getToken());
        Toast.makeText(LogInActivity.this,  preferences.getKeyToken(), Toast.LENGTH_LONG).show();
        redirectHomePage();
        finish();
    }

    public void getKeyToken(String login, String password) {
        User user = new User(login, password,"","","","","");
        preferences.removePreferences(Extra.KEY_TOKEN);
        trackerController.geToken(user);

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

                Intent intent = new Intent(LogInActivity.this, ServiceSettings.class);
                startActivity(intent);

            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


}

