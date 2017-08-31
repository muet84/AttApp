package com.ibrahim.mibrahim.attapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by M.Ibrahim on 4/6/2017.
 */

public class SendSmsService extends Service {

    AlarmHelper alarmHelper;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    SmsNotifier smsNotifier;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmHelper = AlarmHelper.getInstance(this);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(this);
        smsNotifier = new SmsNotifier();
        Toast.makeText(this, "Create Service", Toast.LENGTH_SHORT).show();
        int quantity = sharedPrefrencesHelper.getInt(DATA.STRING_SMS_QUANTITY, 0);
        sendSms(quantity);
        smsNotifier.notify(this, "Sending Sms", "Sms Content", R.drawable.ic_verified_user_white_24dp, 1, 0, 100);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }


    void sendSms(int quantity) {

        DATA.SMS_QUANTITY = quantity;
        sharedPrefrencesHelper.setInt(DATA.SMS_TOTAL_QUANTITY, quantity);
        sharedPrefrencesHelper.setInt(DATA.SMS_COUNTER, 0);
        alarmHelper.setAlarm();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        alarmHelper.destroyAlarm();

    }
}

