package com.ibrahim.mibrahim.attapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by M.Ibrahim on 4/6/2017.
 */

public class SyncQRService extends Service {

    SharedPrefrencesHelper sharedPrefrencesHelper;
    SmsNotifier smsNotifier;
    PostData postData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        postData = new PostData(this);

        smsNotifier = new SmsNotifier();

        if(isNetworkAvailable()) {
        smsNotifier.notify(this,"Updating QR To Server","Internet Should Remain Active",
                R.drawable.ic_verified_user_white_24dp,2,10,100);

        postData.syncQRTable(0);
        }else {
            smsNotifier.notify(this, "Internet Connection Failed", "Try Again after connecting to Internet",
                    R.drawable.ic_verified_user_white_24dp, 2, 100, 100);
            stopSelf();

        }


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
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();

    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            return true;
        }

        return false;
    }

}

