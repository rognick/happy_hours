package com.winify.happy_hours.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.List;

public class WifiListActivity extends Activity {

    private WifiManager mainWifiObj;
    private WifiScanReceiver wifiReceiver;
    private ListView list;
    private String array;
    private ApplicationPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_list);

        preferences = new ApplicationPreferences(this);
        list = (ListView) findViewById(R.id.wifiList);
        list.setBackgroundColor(Color.BLACK);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (list.getItemAtPosition(myItemInt));
                Toast.makeText(getApplicationContext(), selectedFromList + " has been added to wifi list",
                        Toast.LENGTH_LONG).show();

                array = preferences.getWifiListPreferred();
                preferences.removeWifiList();
                preferences.saveWifiList(array + selectedFromList + ",");
                Intent intent = new Intent(WifiListActivity.this, SettingsActivity.class);
                startActivity(intent);

                WifiListActivity.this.finish();

            }
        });

        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiScanReceiver();
        mainWifiObj.startScan();
    }

    protected void onPause() {
        unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReceiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            String[] wifiList = new String[wifiScanList.size()];
            for (int i = 0; i < wifiScanList.size(); i++) {
                wifiList[i] = (wifiScanList.get(i)).SSID;
            }
            list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.item_list, wifiList));
        }
    }
}
