package com.winify.happy_hours.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.winify.happy_hours.R;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowUserInfoActivity extends Activity implements ServiceListener, View.OnClickListener {

    private ApplicationPreferencesActivity preferences;
    private Button button;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText birthDay;
    private EditText phoneNumber;
    private EditText position;
    private EditText team;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_settings);
        preferences= new ApplicationPreferencesActivity(this);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        birthDay = (EditText) findViewById(R.id.birthDay);
        phoneNumber = (EditText) findViewById(R.id.mobile);
        position = (EditText) findViewById(R.id.position);
        team = (EditText) findViewById(R.id.team);
        button = (Button) findViewById(R.id.editBtn);
        button.setOnClickListener(this);

        populateWithData();

        disableEditText();
    }

    @Override
    public void onClick(View click) {
        switch (click.getId()) {
            case R.id.editBtn:
                if (button.getText().equals("Edit")) {

                    button.setText("Save Changes");
                    preferences.editPermission(lastName, true, true);
                    preferences.editPermission(email, true, true);
                    preferences.editPermission(phoneNumber, true, true);

                } else if (button.getText().equals("Save Changes")) {

                    button.setText("Edit");
                    preferences.updatePreferences(Extra.KEY_FIRST_NAME, firstName);
                    preferences.updatePreferences(Extra.KEY_LAST_NAME, lastName);
                    preferences.updatePreferences(Extra.KEY_EMAIL, email);
                    preferences.updatePreferences(Extra.KEY_BIRTHDAY, birthDay);
                    preferences.updatePreferences(Extra.KEY_PHONE_NUMBER, phoneNumber);
                    preferences.updatePreferences(Extra.KEY_POSITION, position);
                    preferences.updatePreferences(Extra.KEY_TEAM, team);

                    disableEditText();
                }
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
    public void onUsersList(User user) {

    }



    public void disableEditText() {
        preferences.editPermission(firstName, false, false);
        preferences.editPermission(lastName, false, false);
        preferences.editPermission(email, false, false);
        preferences.editPermission(birthDay, false, false);
        preferences.editPermission(phoneNumber, false, false);
        preferences.editPermission(position, false, false);
        preferences.editPermission(team, false, false);
    }

    public void populateWithData() {

        firstName.setText(preferences.getStringValueFromPreferences(Extra.KEY_FIRST_NAME));
        lastName.setText(preferences.getStringValueFromPreferences(Extra.KEY_LAST_NAME));
        email.setText(preferences.getStringValueFromPreferences(Extra.KEY_EMAIL));
        birthDay.setText(preferences.getStringValueFromPreferences(Extra.KEY_BIRTHDAY));
        phoneNumber.setText(preferences.getStringValueFromPreferences(Extra.KEY_PHONE_NUMBER));
        position.setText(preferences.getStringValueFromPreferences(Extra.KEY_POSITION));
        team.setText(preferences.getStringValueFromPreferences(Extra.KEY_TEAM));
    }
}
