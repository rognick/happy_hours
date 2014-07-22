package com.winify.happy_hours.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.ApplicationPreferencesActivity;
import com.winify.happy_hours.constants.Extra;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceSettings extends Activity implements ServiceListener, View.OnClickListener {

    public CheckBox checkBox;
    String array;
    private EditText ip;
    private EditText port;
    private Button edit;
    private ApplicationPreferencesActivity preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.service_settings);
        super.onCreate(savedInstanceState);
        preferences = new ApplicationPreferencesActivity(this);


        array = preferences.getWifiListPreferred();
        final List<String> wifiList = new ArrayList<String>();
        wifiList.addAll(Arrays.asList(array.split("\\s*,\\s*")));

        ListView list = (ListView) findViewById(R.id.preferencedWifiList);
        View footer = View.inflate(this, R.layout.settings_list_footer, null);
        list.addFooterView(footer);
        View header = View.inflate(this, R.layout.settings_list_header, null);
        list.addHeaderView(header);
        checkBox = (CheckBox) findViewById(R.id.checkNotification);

        if (preferences.getNotificationStatus().equals(false)) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }

        list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                R.layout.item_list, wifiList));

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                wifiList.remove(pos - 1);

                StringBuilder sb = new StringBuilder();
                for (String s : wifiList) {
                    sb.append(s);
                    sb.append(",");
                }
                preferences.removePreferences(Extra.KEY_WIFI_LIST_PREFERRED);
                preferences.savePreferences(Extra.KEY_WIFI_LIST_PREFERRED, sb.toString());


                Intent intent = new Intent(ServiceSettings.this, ServiceSettings.class);
                startActivity(intent);

                ServiceSettings.this.finish();
                return true;
            }
        });

        ip = (EditText) findViewById(R.id.ipAdress);
        port = (EditText) findViewById(R.id.port);
        edit = (Button) findViewById(R.id.editServiceBtn);
        edit.setOnClickListener(this);
        Button wifiBtn = (Button) findViewById(R.id.addWifi);
        wifiBtn.setOnClickListener(this);


        ip.setText(preferences.getIp());
        port.setText(preferences.getPort());


        preferences.editPermission(ip, false, false);
        preferences.editPermission(port, false, false);

    }

    @Override
    public void onClick(View click) {


        switch (click.getId()) {
            case R.id.editServiceBtn:
                if (edit.getText().equals("Edit")) {

                    edit.setText("Save Changes");
                    preferences.editPermission(ip, true, true);
                    preferences.editPermission(port, true, true);

                } else if (edit.getText().equals("Save Changes")) {


                    edit.setText("Edit");


                    preferences.updatePreferences(Extra.KEY_IP, ip);
                    preferences.updatePreferences(Extra.KEY_PORT, port);

                    preferences.editPermission(ip, false, false);
                    preferences.editPermission(port, false, false);


                }
                break;

            case R.id.addWifi: {

                Intent intent = new Intent(ServiceSettings.this, WifiPreferences.class);
                startActivity(intent);
                ServiceSettings.this.finish();

            }
            break;
        }
    }

    public void onCheckBoxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkNotification:
                if (checked) {

                    preferences.removePreferences(Extra.Notification_Status);
                    preferences.savePreferences(Extra.Notification_Status, true);

                } else {

                    preferences.removePreferences(Extra.Notification_Status);
                    preferences.savePreferences(Extra.Notification_Status, false);

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

}