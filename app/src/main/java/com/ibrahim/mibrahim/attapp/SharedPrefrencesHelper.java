package com.ibrahim.mibrahim.attapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by M.Ibrahim on 3/25/2017.
 */

public class SharedPrefrencesHelper  {

    Context context;
    SharedPreferences preferences;
    String USER_PREFRENCES = "USER_PREFRENCES";
    SharedPreferences.Editor editor;
    private static SharedPrefrencesHelper instance = null;
    public static String DEVICE_ID = "device_key";
    public static String USER_ID = "userid";
    public static String PASSWORD_= "password";




    private SharedPrefrencesHelper(Context context){

        this.context = context;
        preferences = context.getSharedPreferences(USER_PREFRENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();;

    }

    public static SharedPrefrencesHelper getInstance(Context context) {

        if (instance == null) {
            instance = new SharedPrefrencesHelper(context);
            }

        return instance;
    }


    public void setValue(String key, String value){

        editor.putString(key,value);
        editor.commit();
    }

    public String getValue(String key,String defaultvalue){


        String value = preferences.getString(key,defaultvalue);

        return value;
    }

    public void setInt(String key, int value){

        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key,int defaultvalue){

        int value = preferences.getInt(key,defaultvalue);

        return value;
    }




    public boolean checkSync(){

         SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean syncronized = sharedPreferences.getBoolean("syncronized",false);


        return syncronized;
    }



}
