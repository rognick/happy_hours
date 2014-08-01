package com.winify.happy_hours;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.winify.happy_hours.constants.Constants;

public class ApplicationPreferences {

    private final SharedPreferences.Editor editor;
    private SharedPreferences settings;

    public ApplicationPreferences(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = settings.edit();
    }

    public void saveToken(String token) {
        editor.putString(Constants.KEY_TOKEN, token).commit();
        editor.commit();
    }

    public void setTimer(boolean isTimerSet) {
        editor.putBoolean(Constants.KEY_TIMER, isTimerSet);
        editor.commit();
    }

    public String getKeyToken() {
        return settings.getString(Constants.KEY_TOKEN, "");
    }


    public String getIp() {
        return settings.getString(Constants.KEY_IP, "");
    }

    public String getPort() {
        return settings.getString(Constants.KEY_PORT, "");
    }


    public boolean isTimerSet() {
        return settings.getBoolean(Constants.KEY_TIMER, false);
    }

    public void removeToken() {
        settings.edit().remove(Constants.KEY_TOKEN).commit();
    }
}





