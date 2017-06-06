package kpi.obalitskyi.diploma;

/**
 * Created by obalitskyi on 5/30/17.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Window;

import kpi.obalitskyi.diploma.preferences.NumberPickerPreference;


public class PrefsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String default_preferences_name = "kpi.obalitskyi.diploma";

    protected void onCreate(Bundle savedInstanceState) {
        // fix error white-text-on_white-background-in-some-cases
        setDeviceTheme();

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        this.getPreferenceManager().setSharedPreferencesName(default_preferences_name);
//this.getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        final SharedPreferences prefs = this.getPreferenceManager().getSharedPreferences();

        EditTextPreference etp = (EditTextPreference) findPreference("pref_doctor_email");
        etp.setSummary(prefs.getString("pref_doctor_email", ""));

        etp = (EditTextPreference) findPreference("email_subject");
        etp.setSummary(prefs.getString("email_subject", ""));

        etp = (EditTextPreference) findPreference("email_text");
        etp.setSummary(prefs.getString("email_text", ""));

        NumberPickerPreference npp = (NumberPickerPreference) findPreference("stroke_width");
        npp.setSummary(String.valueOf(prefs.getInt("stroke_width", 10)));
    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs,
                                          String key) {

        Preference etp = findPreference(key);
        if (key.equals("stroke_width")){
            etp.setSummary(String.valueOf(prefs.getInt(key, 10)));
        }else {
            etp.setSummary(prefs.getString(key, ""));
        }

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void setDeviceTheme() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setTheme(android.R.style.Theme_DeviceDefault);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setTheme(android.R.style.Theme_Holo);
            } else setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        } catch (Throwable e) {
            setTheme(android.R.style.Theme_Holo);
        }
    }
}