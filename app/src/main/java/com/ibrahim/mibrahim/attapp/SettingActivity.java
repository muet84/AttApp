package com.ibrahim.mibrahim.attapp;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;

public class SettingActivity extends PreferenceActivity {


    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addPreferencesFromResource(R.xml.setting_preferences);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        addPreferencesFromResource(R.xml.setting_preferences);
        initPreferences();


//        settingPreferences.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object o) {
//                Toast.makeText(SettingActivity.this, "" + "\n", Toast.LENGTH_SHORT).show();
//
//                if (preference instanceof CheckBoxPreference) {
//                    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
//                    boolean isChecked = checkBoxPreference.isChecked();
//                    String key = checkBoxPreference.getKey();
//
//                    Toast.makeText(SettingActivity.this, ""+isChecked+"\n" + key + "\n", Toast.LENGTH_SHORT).show();
//
//            }
//                return true;
//        }
//
//
//    });

}
    private void initPreferences() {
       // PreferenceManager.setDefaultValues(this, R.xml.setting_preferences, true);

//        if (!sharedPreferences.contains("synchronized_check")) {
//
//            Toast.makeText(this, "False Contains", Toast.LENGTH_SHORT).show();
//        }

        findPreference("synchronized_check").setSummary("enable or disable synchronization");

        findPreference("synchronized_check").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

//                if (preference instanceof CheckBoxPreference) {
//                    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
//                    boolean isChecked = !checkBoxPreference.isChecked();
//                    String key = checkBoxPreference.getKey();
//
//                    sharedPreferences.edit().putBoolean("syncronized",isChecked).commit();
//                    boolean syncronized = sharedPreferences.getBoolean("syncronized",false);
//
//                    Log.i("syncCheck",""+isChecked+"\n" + key + "\n"+syncronized);
//
//                }
                if (preference instanceof EditTextPreference) {
                    EditTextPreference editTextPreference = (EditTextPreference) preference;
                    //boolean isChecked = !checkBoxPreference.isChecked();
                    String key = editTextPreference.getKey();

                    sharedPreferences.edit().putString("pref_sms",editTextPreference.getText()).commit();

                    Log.i("smsQuantity",""+editTextPreference.getText()+"\n" + key );

                }

                return newValue != null && !newValue.equals("");
            }
        });

    }

}
