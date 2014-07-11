package com.winify.happy_hours.activities.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.service.LogInActivity;
import com.winify.happy_hours.activities.constants.Extra;

import java.util.List;

/**
 * Created by nicolaerogojan on 7/9/14.
 */
public class WifiService extends Service {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;
    String wifis[];


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is

        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        mainWifiObj.startScan();

        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

//TODO Delete
        System.out.println("Service Started OK");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReciever);
//        TODO Delete
        System.out.println("Service Destroyed ");
    }


    class WifiScanReceiver extends BroadcastReceiver {

        SharedPreferences settings;
        public static final String PREFS_NAME = "LoginPrefs";


        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {

            System.out.println("Wifi Broadcast");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();

            WifiInfo wifiInfo = mainWifiObj.getConnectionInfo();


            if (wifiInfo.getSSID().toUpperCase().toString().equals("\"" + Extra.MyNetwork + "\"") ||
                    wifiInfo.getSSID().toUpperCase().toString().equals(Extra.MyNetwork)) {

//TODO delete
                System.out.println("====>  Wifi action  ");
//          if login
                if (settings.getString("timer", "").equals("true")) {
                    notification("Status", "Timer ON", true);

                } else if (settings.getString("timer", "").equals("false")) {
                    notification("Status", "Timer OFF", true);
                }

            } else {
                System.out.println("====>  Wifi not conect");

                for (int i = 0; i < wifiScanList.size(); i++) {
                    if (wifiScanList.get(i).SSID.toUpperCase().equals(Extra.MyNetwork)) {
                        notification("Network", wifiScanList.get(i).SSID, false);
                    }
                }
            }
        }

        public void notification(String contentTitle, String contentText, Boolean onGoing) {

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Prepare intent which is triggered if the
            // notification is selected
            Intent intent2 = new Intent(WifiService.this, LogInActivity.class);
            if (!onGoing) {
                intent2 = new Intent(Settings.ACTION_WIFI_SETTINGS);
            }
            PendingIntent pIntent = PendingIntent.getActivity(WifiService.this, 0, intent2, 0);
            // Build notification
            // Actions are just fake
            Notification noti = new Notification.Builder(WifiService.this)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setSmallIcon(R.drawable.winify)
                    .setContentIntent(pIntent)
                    .setOngoing(onGoing)

                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (!onGoing) {
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                noti.defaults |= Notification.DEFAULT_VIBRATE;
                noti.defaults |= Notification.DEFAULT_SOUND;
            }
            notificationManager.notify(0, noti);
        }
    }

}
