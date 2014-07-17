package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import com.winify.happy_hours.service.ServiceSettings;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends Activity implements ServiceListener, View.OnClickListener {

    private ApplicationPreferencesActivity preferences;

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
        preferences= new ApplicationPreferencesActivity(this);
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onServerFail(RetrofitError error) {
    }

    @Override
    public void getUser(User user) {
    }

    @Override
    public void onClick(View click) {

        switch (click.getId()) {

            case R.id.logoutBtn: {

                preferences.removePreferences(Extra.KEY_USER_NAME);
                preferences.removePreferences(Extra.KEY_PASSWORD);


                Intent intent = new Intent(SettingsActivity.this,LogInActivity.class);
                startActivity(intent);

            }
            break;

            case R.id.userSettingsBtn: {


                Intent intent = new Intent(SettingsActivity.this,ShowUserInfoActivity.class);
                startActivity(intent);


            }
            break;

            case R.id.serviceBtn: {


                Intent intent = new Intent(SettingsActivity.this,ServiceSettings.class);
                startActivity(intent);


            }
            break;
        }
    }
}
