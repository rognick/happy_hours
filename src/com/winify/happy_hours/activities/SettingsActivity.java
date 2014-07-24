package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.controller.TrackerController;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends Activity implements ServiceListener, View.OnClickListener {

    private ApplicationPreferences preferences;
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
        preferences = new ApplicationPreferences(this);
        ServiceGateway serviceGateway = new ServiceGateway(SettingsActivity.this);
        trackerController = serviceGateway.getTrackerController(this);
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onServerFail(RetrofitError error) {
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

            }
            break;
            case R.id.userSettingsBtn: {

                Intent intent = new Intent(SettingsActivity.this, ShowUserInfoActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.serviceBtn: {
                Intent intent = new Intent(SettingsActivity.this, ServiceSettingsActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
