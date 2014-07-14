package com.winify.happy_hours.activities.service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class ServiceSettings extends Prefs   implements ServiceListener, View.OnClickListener{
    private EditText ip;
    private EditText port;
    SharedPreferences settings;
    private Button edit;

    public static String ipAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.service_settings);

        super.onCreate(savedInstanceState);

        ip= (EditText) findViewById(R.id.ipAdress);
        port= (EditText) findViewById(R.id.port);



        edit= (Button) findViewById(R.id.editServiceBtn);

        edit.setOnClickListener(this);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        ip.setText(settings.getString("ip", "").toString());

        port.setText(settings.getString("port", "").toString());


        ipAdress="htttp://"+ settings.getString("ip", "").toString()+ ":"+settings.getString("port", "").toString();


        editPermision(ip, false, false);
        editPermision(port, false, false);


    }

    @Override
    public void onClick(View click) {


        switch (click.getId()) {
            case R.id.editServiceBtn:


                if (edit.getText().equals("Edit")) {

                    System.out.print("Edited");
                    edit.setText("Save Changes");

                    editPermision(ip, true, true);
                    editPermision(port, true, true);









                } else if (edit.getText().equals("Save Changes")) {

                    System.out.print("Changed");
                    edit.setText("Edit");



                    updatePrefs("ip",ip);

                    updatePrefs("port",port);




                    editPermision(ip, false, false);
                    editPermision(port, false, false);






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
