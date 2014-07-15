package com.winify.happy_hours.activities.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.controller.Prefs;
import com.winify.happy_hours.activities.listeners.ServiceListener;
import com.winify.happy_hours.activities.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Cornel on 7/14/2014.
 */
public class ServiceSettings extends Prefs   implements ServiceListener, View.OnClickListener{
    private EditText ip;
    private EditText port;
    SharedPreferences settings;
    private Button edit;
    private Button wifiBtn;
    String array;


    private ListView list;
    public static String ipAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.service_settings);

        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        array =settings.getString("wifi_list_prefered", "").toString();


        List<String> wifiList = Arrays.asList(array.split("\\s*,\\s*"));













        list= (ListView) findViewById(R.id.preferencedWifiList);

        //todo complete listview
       list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, wifiList));

        ip= (EditText) findViewById(R.id.ipAdress);
        port= (EditText) findViewById(R.id.port);



        edit= (Button) findViewById(R.id.editServiceBtn);

        edit.setOnClickListener(this);

        wifiBtn= (Button) findViewById(R.id.addWifi);
        wifiBtn.setOnClickListener(this);


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




            case R.id.addWifi :{



                Intent intent = new Intent(ServiceSettings.this, WifiPreferences.class);


                startActivityForResult(intent,1);

            }break;


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
