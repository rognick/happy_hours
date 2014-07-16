package com.winify.happy_hours.activities.service;

import android.annotation.SuppressLint;
import android.content.*;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.controller.Prefs;

import java.util.List;


/**
 * Created by nicolaerogojan on 7/7/14.
 */
public class WifiPreferences extends Prefs {

    /**
     * Called when the activity is first created.
     */

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    ListView list;
    String wifis[];
    String array;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);

        System.out.println("Wifi lisr Start");

        list = (ListView) findViewById(R.id.wifiList);
        list.setBackgroundColor(Color.BLACK);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (list.getItemAtPosition(myItemInt));
                Toast.makeText(getApplicationContext(), selectedFromList + " has been added to wifi list",
                        Toast.LENGTH_LONG).show();


                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                array = settings.getString("wifi_list_prefered", "").toString();


                removePref("wifi_list_prefered");


                editor.putString("wifi_list_prefered", array + selectedFromList + ",");
                editor.commit();


                Intent intent = new Intent(WifiPreferences.this, ServiceSettings.class);
                startActivity(intent);
                WifiPreferences.this.finish();


            }
        });


        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();


    }


    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();

    }


    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            wifis = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {

                wifis[i] = (wifiScanList.get(i)).SSID.toString();
            }

            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.item_list, wifis));


        }


    }

}
