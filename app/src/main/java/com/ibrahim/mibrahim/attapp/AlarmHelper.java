package com.ibrahim.mibrahim.attapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by M.Ibrahim on 4/22/2017.
 */

public class AlarmHelper {

    Context context;
    static AlarmHelper instance;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    SharedPrefrencesHelper prefrencesHelper;

    private AlarmHelper(Context context){

        this.context = context;
        prefrencesHelper = SharedPrefrencesHelper.getInstance(context);
    }

    public static AlarmHelper getInstance(Context context){

        if(instance==null){
            instance= new AlarmHelper(context);
            return instance;

        }else return instance;

    }

    public void destroyAlarm(){
        alarmManager.cancel(alarmIntent);
        alarmManager = null;
        instance =null;

    }

    public void setAlarm(){

        int quantity = prefrencesHelper.getInt(DATA.STRING_SMS_QUANTITY,200);
        long interval =3600/quantity;
        long intervalMillis = interval*1000;

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SmsBroadcast.class), 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, intervalMillis, alarmIntent);
        prefrencesHelper.setValue(DATA.ALARM,"1");


    }
    public void cancelAlarm(){

        if(alarmManager!=null && alarmIntent!=null){

            Toast.makeText(context, "Alaram canceled", Toast.LENGTH_SHORT).show();
            alarmManager.cancel(alarmIntent);
            alarmIntent = null;
            prefrencesHelper.setValue("alarm","0");

        }else Log.i("alarm","Problem With Alarm Manager"); //Toast.makeText(context, "Problem With Alarm Manager", Toast.LENGTH_SHORT).show();

    }


    public boolean isAlarmAlreadySet(){

        if(alarmIntent !=null){
            return true;
        }else return false;

    }





}
