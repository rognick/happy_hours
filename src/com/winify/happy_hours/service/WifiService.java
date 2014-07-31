package com.winify.happy_hours.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.MainActivity;
import com.winify.happy_hours.constants.Constants;

import java.util.Calendar;
import java.util.List;

public class WifiService extends Service {
    public ApplicationPreferences preferences;
    public WifiManager mainWifiObj;
    public WifiScanReceiver wifiReceiver;
    public Boolean isRegistered = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        preferences = new ApplicationPreferences(this);

        if ((dayOfWeek != Calendar.SATURDAY) || (dayOfWeek != Calendar.SUNDAY)) {

            mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifiReceiver = new WifiScanReceiver();
            mainWifiObj.startScan();
            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            isRegistered = true;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isRegistered) {
            unregisterReceiver(wifiReceiver);
        }
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            WifiInfo wifiInfo = mainWifiObj.getConnectionInfo();

            if (wifiInfo.getSSID().toUpperCase().equals("\"" + Constants.MyNetwork + "\"") ||
                    wifiInfo.getSSID().toUpperCase().equals(Constants.MyNetwork)) {

                if (preferences.isTimerSet()) {
                    notification("Status", "Timer ON", true);

                } else {
                    notification("Status", "Timer OFF", true);
                }
            } else {
                for (int i = 0; i < wifiScanList.size(); i++) {
                    if (wifiScanList.get(i).SSID.toUpperCase().equals(Constants.MyNetwork)) {
                        notification("Network", wifiScanList.get(i).SSID, false);
                    }
                }
            }
        }

        public void notification(String contentTitle, String contentText, Boolean onGoing) {

            Intent intent2 = new Intent(WifiService.this, MainActivity.class);
            if (!onGoing) {
                intent2 = new Intent(Settings.ACTION_WIFI_SETTINGS);
            }
            PendingIntent pIntent = PendingIntent.getActivity(WifiService.this, 0, intent2, 0);

            Notification notification = new Notification.Builder(WifiService.this)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setSmallIcon(R.drawable.winify)
                    .setContentIntent(pIntent)
                    .setOngoing(onGoing)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (!onGoing) {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            notificationManager.notify(0, notification);
        }
    }
}
