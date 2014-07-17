package com.winify.happy_hours.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import com.winify.happy_hours.constants.Extra;


public class ApplicationPreferencesActivity  {

    public static final String PREFS_NAME = "LoginPrefs";
    private final SharedPreferences.Editor editor;
    SharedPreferences settings;

    public ApplicationPreferencesActivity(Context context) {
        this.settings = context.getSharedPreferences(PREFS_NAME,Activity.MODE_PRIVATE);
        this.editor = settings.edit();
        editor.commit();

    }

    public void savePreferences(String key, EditText item) {

        editor.putString(key, item.getText().toString());
        editor.commit();
    }

    public void updatePreferences(String key, EditText item) {
        removePreferences(key);
        savePreferences(key, item);
    }

    public String getStringValueFromPreferences(String key) {


        return settings.getString(key, "");
    }

    public void savePreferences(String key, String item) {

        editor.putString(key, item);
        editor.commit();
    }

    public void updatePreferences(String key, String value) {
        removePreferences(key);
        savePreferences(key, value);
    }

    public void savePreferences(String key, Boolean item) {

        editor.putBoolean(key, item);
        editor.commit();
    }

    public void updatePreferences(String key, Boolean value) {
        removePreferences(key);
        savePreferences(key, value);
    }

    public Boolean getBooleanValueFromPreferences(String key) {


        return settings.getBoolean(key, false);
    }

    public void removePreferences(String key) {

        editor.remove(key);
        editor.commit();
    }

    public void editPermission(TextView name, Boolean enable, Boolean focus) {
        name.setFocusableInTouchMode(enable);
        name.setFocusable(focus);
    }

    public void saveCredentials(String userName, String password) {
        savePreferences(Extra.KEY_USER_NAME, userName);
        savePreferences(Extra.KEY_PASSWORD, password);
    }


}





