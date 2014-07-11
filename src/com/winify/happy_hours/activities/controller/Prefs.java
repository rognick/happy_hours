package com.winify.happy_hours.activities.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;


/**
 * Created by Cornel on 7/9/2014.
 */
public class Prefs extends Activity{


    public static final String PREFS_NAME = "LoginPrefs";



    public  void  savePrefs(String key, EditText item) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, item.getText().toString());
        editor.commit();
    }


    public void removePref(String key){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }


    public void savePrefs (String key, String item){

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, item);
        editor.commit();
    }

    public void updatePrefs (String key , EditText item){

        removePref(key);
        savePrefs(key,item);
    }

    public void updatePrefs(String key , String value){

        removePref(key);
        savePrefs(key,value);
    }

}





