package com.ibrahim.mibrahim.attapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by M.Ibrahim on 4/22/2017.
 */

public class SmsBroadcast extends BroadcastReceiver {

    static SmsTableHelper smsTableHelper;
    private PendingIntent smsDeliveryIntent;
    Context context;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    int totalQuantityperHour =0;
    int smsSentCounter=0;
    AlarmHelper alarmHelper;
    SmsNotifier smsNotifier;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        smsTableHelper = new SmsTableHelper(context);
        //Toast.makeText(context, "Sending Message", Toast.LENGTH_SHORT).show();
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(context);
        alarmHelper = AlarmHelper.getInstance(context);
        smsNotifier = new SmsNotifier();

     //  if(validateQuantity()) {

           try {
               sendSms();
           }catch (Exception e){
               Log.i("exception",e.getMessage());
           }


       //}else{
          // Log.i("sent","All sms sent for this hour");
           //Toast.makeText(context, "All sms sent for this hour", Toast.LENGTH_SHORT).show();
          // alarmHelper.cancelAlarm();
           //smsTableHelper.changeSmsStatus(-1,1);

       //}
    }

    boolean validateQuantity(){

        totalQuantityperHour = sharedPrefrencesHelper.getInt(DATA.SMS_TOTAL_QUANTITY,-1);
        smsSentCounter = sharedPrefrencesHelper.getInt(DATA.SMS_COUNTER,-1);

        Log.i("smscounter", "smsSentCounter" + smsSentCounter + "\n" + "totalQuantityperHour" +totalQuantityperHour);


        if(totalQuantityperHour!=-1 && smsSentCounter!=-1){

            if(smsSentCounter<totalQuantityperHour){

                return true;

            }else return false;

        }else return false;

    }

    void sendSms() {

        SmsBean smsTosend = smsTableHelper.getSms();
//        smsDeliveryIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SmsDeliveryBroadcast.class), 0);

        if (smsTosend != null) {
            smsNotifier.notify(context,"Sending Sms","Sms Content Updated",R.drawable.ic_verified_user_white_24dp,
                    1,smsTableHelper.getSentSmsCount(),smsTableHelper.getAllSmsCount());

            if (smsTosend.getTo_number() != null && !smsTosend.getTo_number().equals("")
                    && !smsTosend.getTo_number().equals("null") && !(smsTosend.getTo_number().length()<13)) {


                String smsNum = smsTosend.getTo_number();
                String smsTitle = smsTosend.getTitle();
                String smsContent = smsTosend.getContents();
                String smsFrom = smsTosend.getFrom_text();
                String smsId = smsTosend.getId();
                String shortContent = "";


                if(smsNum.length()>13){
                    String temp_num = smsNum.substring(0,12);
                    smsNum = temp_num;

                }

                if (smsContent.length() > 159) {

                    //shortContent = smsContent.substring(0,159);
                    shortContent = smsContent.split(" . ")[0];
                    shortContent = shortContent + ".";
                    smsContent = shortContent;

                    if (shortContent.length() > 159) {
                        String tempContent = shortContent.substring(0, 159);
                        shortContent = tempContent + ".";
                        smsContent = shortContent;
                    }
                }

                // String testContent = "Dear Parent, Monthly Fee PKR600 is due for month of March 2018 of your Student maria Mushtaq ahmed at Latif Niazi Memorial Higher secondary school .";

                String completeSms = smsContent;

                Intent intent = new Intent(context, SmsDeliveryBroadcast.class);
                intent.putExtra("SMS_ID", smsId);

                Log.i("idrecieved", smsId);
                DATA.SMS_ID = smsId;

                smsDeliveryIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

                Log.i("sms", "\n" + completeSms + "\n" + "SMS NUM:" + smsNum + "\n" +
                        smsTosend.toString() + "sms before" + "Length" + smsContent.length() + "\n short Content" + shortContent.length());

                SmsManager sms = SmsManager.getDefault();


                 ArrayList<String> sms_parts = sms.divideMessage(completeSms);
                 int numParts = sms_parts.size();


//                ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
//                ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
//
//                for (int i = 0; i < numParts; i++) {
//                    sentIntents.add(PendingIntent.getBroadcast(getContext(), 0, mSendIntent, 0));
//                    deliveryIntents.add(PendingIntent.getBroadcast(getContext(), 0, mDeliveryIntent, 0));
//                }

//yess

                sms.sendMultipartTextMessage(smsNum,null,sms_parts,null,null);



                sms.sendTextMessage(smsNum, null, completeSms, null, smsDeliveryIntent);
                updateSentSms(smsTosend.getId());

                Log.i("sms" + "\n" + completeSms + "\n" + smsNum, smsTosend.toString() + "sms after");

                int previousCounter = sharedPrefrencesHelper.getInt(DATA.SMS_COUNTER, 0);
                Log.i("smscounter", "Previous" + previousCounter);

                previousCounter++;
                sharedPrefrencesHelper.setInt(DATA.SMS_COUNTER, previousCounter);


                Log.i("smscounter", "Counter Now" + previousCounter);

            }else updateFailedSms(smsTosend.getId());

            } else {

                //alarmHelper.cancelAlarm();
                context.stopService(new Intent(context,SendSmsService.class));
                context.startService(new Intent(context,SyncSmsService.class));
                smsTableHelper.changeSmsStatus(-1, 1);
                Log.i("smsnull", "null sms");


            }

    }

    void updateFailedSms(String sms_id){

        smsTableHelper.updateSmsStatus(Integer.parseInt(sms_id),-10);
        Log.i("idToQuery",sms_id);
    }
    void updateSentSms(String sms_id){

        smsTableHelper.updateSmsStatus(Integer.parseInt(sms_id),5);
        Log.i("idToQuery",sms_id);
    }

}
