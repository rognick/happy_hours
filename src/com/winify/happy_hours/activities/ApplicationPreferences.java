package com.winify.happy_hours.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.winify.happy_hours.constants.Extra;

public class ApplicationPreferences {

    private final SharedPreferences.Editor editor;
    private SharedPreferences settings;

    public ApplicationPreferences(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = settings.edit();
    }

    public void saveToken(String token) {
        editor.putString(Extra.KEY_TOKEN, token).commit();
        editor.commit();
    }

    public void saveWifiList(String wifiList) {
        editor.putString(Extra.KEY_WIFI_LIST_PREFERRED, wifiList);
        editor.commit();
    }

    public void setTimer(boolean isTimerSet) {
        editor.putBoolean(Extra.KEY_TIMER, isTimerSet);
        editor.commit();
    }

    public String getKeyToken() {
        return settings.getString(Extra.KEY_TOKEN, "");
    }

    public String getWifiListPreferred() {
        return settings.getString(Extra.KEY_WIFI_LIST_PREFERRED, "");
    }

    public String getIp() {
        return settings.getString(Extra.KEY_IP, "");
    }

    public String getPort() {
        return settings.getString(Extra.KEY_PORT, "");
    }

    public boolean isNotificationStatusSet() {
        return settings.getBoolean(Extra.Notification_Status, false);
    }

    public boolean isTimerSet() {
        return settings.getBoolean(Extra.KEY_TIMER, false);
    }

    public void removeToken() {
        settings.edit().remove(Extra.KEY_TOKEN).commit();
    }

    public void removeWifiList() {
        settings.edit().remove(Extra.KEY_WIFI_LIST_PREFERRED).commit();
    }
}





