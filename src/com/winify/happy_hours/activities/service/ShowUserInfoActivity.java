package com.winify.happy_hours.activities.service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.controller.Prefs;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

/**
 * Created by Cornel on 7/8/2014.
 */
public class ShowUserInfoActivity extends Prefs implements ServiceListener, View.OnClickListener {


    SharedPreferences settings;
    private Button button;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText birthDay;
    private EditText mobile;
    private EditText position;
    private EditText team;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_settings);
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        birthDay = (EditText) findViewById(R.id.birthDay);
        mobile = (EditText) findViewById(R.id.mobile);
        position = (EditText) findViewById(R.id.position);
        team = (EditText) findViewById(R.id.team);


        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        button = (Button) findViewById(R.id.editBtn);

        button.setOnClickListener(this);


        firstName.setText(settings.getString("firstName", "").toString());

        lastName.setText(settings.getString("lastName", "").toString());

        email.setText(settings.getString("email", "").toString());

        birthDay.setText(settings.getString("birthDay", "").toString());

        mobile.setText(settings.getString("mobile", "").toString());

        position.setText(settings.getString("position", "").toString());

        team.setText(settings.getString("team", "").toString());


        editPermision(firstName, false, false);
        editPermision(lastName, false, false);
        editPermision(email, false, false);
        editPermision(birthDay, false, false);
        editPermision(mobile, false, false);
        editPermision(position, false, false);
        editPermision(team, false, false);




    }


    @Override
    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.editBtn:


                if (button.getText().equals("Edit")) {

                    System.out.print("Edited");
                    button.setText("Save Changes");

                    editPermision(lastName, true, true);
                    editPermision(email, true, true);

                    editPermision(mobile, true, true);



                } else if (button.getText().equals("Save Changes")) {

                    System.out.print("Changed");
                    button.setText("Edit");



                    updatePrefs("firstName",firstName);

                    updatePrefs("lastName",lastName);
                    updatePrefs("email",email);
                    updatePrefs("birthDay",birthDay);
                    updatePrefs("mobile",mobile);
                    updatePrefs("position",position);
                    updatePrefs("team",team);




                    editPermision(firstName, false, false);
                    editPermision(lastName, false, false);
                    editPermision(email, false, false);
                    editPermision(birthDay, false, false);
                    editPermision(mobile, false, false);
                    editPermision(position, false, false);
                    editPermision(team, false, false);





                }
                break;

            default:
                break;
        }
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





}
