package com.ibrahim.mibrahim.attapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by M.Ibrahim on 4/22/2017.
 */

public class SmsDeliveryBroadcast extends BroadcastReceiver {

    String sms_id="";
    SmsTableHelper smsTableHelper;
    PostData postData;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        //Toast.makeText(context, "Delivery report recieved", Toast.LENGTH_SHORT).show();
        Log.i("deliver","delivery report");

        sms_id = intent.getStringExtra("SMS_ID");

        Log.i("idInIntent",sms_id);

        sms_id = DATA.SMS_ID;

        Log.i("idInData",sms_id);

        smsTableHelper = new SmsTableHelper(context);
        postData = new PostData(context);

        switch (getResultCode()) {

            case RESULT_OK:
//                Toast.makeText(context, "sms_delivered",
//                        Toast.LENGTH_SHORT).show();

                Log.i("deliver","delivered");
                updateSentSms();
                //updateSmsToServer();

                break;

            case RESULT_CANCELED:
//                Toast.makeText(context, "sms_not_delivered",
//                        Toast.LENGTH_SHORT).show();
                Log.i("deliver","not delivered");
                updateFailedSms();
                break;

            default:
//                Toast.makeText(context, "sms Failed",
//                        Toast.LENGTH_SHORT).show();
                Log.i("deliver","failed");
                updateFailedSms();
                break;


        }

    }

    void updateSentSms(){

        smsTableHelper.updateSmsStatus(Integer.parseInt(sms_id),10);
        Log.i("idToQuery",sms_id);
    }

    void updateFailedSms(){

        smsTableHelper.updateSmsStatus(Integer.parseInt(sms_id),-1);
        Log.i("idToQuery",sms_id);
    }

    void updateSmsToServer(){
        if(isNetworkAvailable()){
            postData.updateSmsToServer(Integer.parseInt(sms_id));
        }else {}
    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            if(networkInfo.getState()== NetworkInfo.State.CONNECTED){
            return true;
             }
        }

        return false;
    }

}
