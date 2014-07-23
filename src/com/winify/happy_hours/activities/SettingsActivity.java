package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
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

public class SettingsActivity extends Activity implements ServiceListener, View.OnClickListener {

    private ApplicationPreferencesActivity preferences;
    private TrackerController trackerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.settings);
        super.onCreate(savedInstanceState);
        Button logout = (Button) findViewById(R.id.logoutBtn);
        Button userInfo = (Button) findViewById(R.id.userSettingsBtn);
        Button serviceEndPoint = (Button) findViewById(R.id.serviceBtn);

        logout.setOnClickListener(this);
        userInfo.setOnClickListener(this);
        serviceEndPoint.setOnClickListener(this);
        preferences = new ApplicationPreferencesActivity(this);

        ServiceGateway serviceGateway = new ServiceGateway(SettingsActivity.this);
        trackerController = serviceGateway.getTrackerController(this);
    }

    @Override
    public void onSuccess(Response response) {
        Toast.makeText(SettingsActivity.this, "Server OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerFail(RetrofitError error) {
        Toast.makeText(SettingsActivity.this, "Server FAIL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUsersList(User user) {
    }

    @Override
    public void onClick(View click) {

        switch (click.getId()) {

            case R.id.logoutBtn: {
                User user = new User("", "", preferences.getKeyToken(), "", "", "", "");
                trackerController.logOut(user);
                preferences.removePreferences(Extra.KEY_TOKEN);
                Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                startActivity(intent);




                finish();
            }
            break;

            case R.id.userSettingsBtn: {

                Intent intent = new Intent(SettingsActivity.this, ShowUserInfoActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.serviceBtn: {
                Intent intent = new Intent(SettingsActivity.this, ServiceSettings.class);
                startActivity(intent);
            }
            break;
        }
    }
}
