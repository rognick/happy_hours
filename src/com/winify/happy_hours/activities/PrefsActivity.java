package com.winify.happy_hours.activities;



import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.winify.happy_hours.R;

public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}