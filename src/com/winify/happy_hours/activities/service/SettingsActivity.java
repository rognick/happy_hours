package com.winify.happy_hours.activities.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.controller.Prefs;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by Cornel on 7/14/2014.
 */
public class SettingsActivity extends Prefs implements ServiceListener, View.OnClickListener {


    private Button logout;
    private Button userInfo;


    private Button serviceEndPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.settings);
        super.onCreate(savedInstanceState);
        logout = (Button)findViewById(R.id.logoutBtn);
        userInfo= (Button) findViewById(R.id.userSettingsBtn);

        serviceEndPoint=(Button)findViewById(R.id.serviceBtn);


        logout.setOnClickListener(this);
        userInfo.setOnClickListener(this);

        serviceEndPoint.setOnClickListener(this);




    }

    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onServerFail(RetrofitError error) {

    }

    @Override
    public void onUsersList(List<User> list) {

    }

    @Override
    public void onClick(View click) {

        switch (click.getId()) {


            case R.id.logoutBtn: {

                removePref("login");

                removePref("password");


                Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);


                startActivityForResult(intent,1);


            }
            break;

            case R.id.userSettingsBtn: {

                Intent intent = new Intent(SettingsActivity.this, ShowUserInfoActivity.class);


                startActivityForResult(intent,1);


            }
            break;



            case  R.id.serviceBtn:{


                Intent intent = new Intent(SettingsActivity.this, ServiceSettings.class);


                startActivityForResult(intent,1);


            }



            default:break;

        }


    }
}
