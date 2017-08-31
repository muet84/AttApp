package com.ibrahim.mibrahim.attapp;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener {

    SyncData syncData;
    SmsTableHelper smsTableHelper;
    Button fetchsms,sendsms,cancelsms,openSms,updateSms;
    SmsBroadcast smsBroadCast;
    //AlarmHelper alarmHelper;
    Dialog dialog;
    ProgressDialog progressDialog;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    String isAlaramSet;
    TextView sms_info;
    PostData postData;
    private SharedPreferences defaultSharedPrefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this, "welcome", Toast.LENGTH_SHORT).show();

        fetchsms = (Button) findViewById(R.id.fetchsms);
        sendsms = (Button) findViewById(R.id.sendsms);
        cancelsms = (Button) findViewById(R.id.cancelsms);
        openSms = (Button) findViewById(R.id.openSms);
        sms_info = (TextView) findViewById(R.id.sms_info);
        updateSms = (Button) findViewById(R.id.update_sms);
        progressDialog = new ProgressDialog(this);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(this);
        postData = new PostData(this);

        String str_permissions = sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS,"");

        if(str_permissions.contains("message")){

        }else showDialog("Sorry , You dont have permissions for Messages");


        isAlaramSet = sharedPrefrencesHelper.getValue(DATA.ALARM,"0");

        if(isMyServiceRunning(SendSmsService.class)){
            sendsms.setEnabled(false);
        }
        if(isMyServiceRunning(SyncSmsService.class)){
            updateSms.setEnabled(false);
        }

        if(isAlaramSet.equals("1")){
         //   sendsms.setEnabled(false);

        }
        fetchsms.setOnClickListener(this);
        sendsms.setOnClickListener(this);
        cancelsms.setOnClickListener(this);
        openSms.setOnClickListener(this);
        updateSms.setOnClickListener(this);
//        smsBroadCast = new SmsBroadcast();
//        IntentFilter f = new IntentFilter("action_sms");
//        registerReceiver(smsBroadCast,f);
//        SmsDeliveryBroadcast deliveryBroadcast = new SmsDeliveryBroadcast();
//        IntentFilter f = new IntentFilter("action_delivery");
//        registerReceiver(smsBroadCast,f);

//

        //alarmHelper = AlarmHelper.getInstance(this);

        syncData = new SyncData(this);
        smsTableHelper = new SmsTableHelper(this);

        defaultSharedPrefrence = PreferenceManager.getDefaultSharedPreferences(this);

//        sms_info.setText("Sms information :" + "\n" + "Total Sms : "+ smsTableHelper.getAllSmsCursor(false).getCount()
//            +"\n"+"Sent Sms : " + smsTableHelper.getSentSmsCursor().getCount()
//            +"\n" + "Unsent Sms :" + smsTableHelper.getUnSentSmsCursor().getCount()
//            +"\n" + "Permanent Failed Sms : " +smsTableHelper.getPermenantFailedSmsCursor().getCount()
//            +"\n" + "Sent Not Updated :" + smsTableHelper.getSentNotUpdatedSmsCursor().getCount()
//            +"\n" + "Updated :" + smsTableHelper.getLateUpdatedSmsCursor().getCount()
//
//        );
    }
    void showDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setTitle(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);
    }


    void fetchSms(){

        showProgressDialog();

        syncData.syncMessages(new onMessagesComplete() {
            @Override
            public void onSuccess(ArrayList<SmsBean> smsList) {

//                smsTableHelper.insertMessages(smsList);
                new SmsAsyncTask(smsList).execute();
                //hideProgressDialog();

            }
            @Override
            public void onFailue(Throwable error) {
                hideProgressDialog();
                showComletionDialog("An error occured while fetching sms "+"\n" + "Please check your Internet Connection");

            }
        });

    }

    void sendSms(int quantity){

        DATA.SMS_QUANTITY = quantity;
        sharedPrefrencesHelper.setInt(DATA.STRING_SMS_QUANTITY,quantity);
        startService(new Intent(SmsActivity.this,SendSmsService.class));

//        sharedPrefrencesHelper.setInt(DATA.SMS_TOTAL_QUANTITY,quantity);
//        sharedPrefrencesHelper.setInt(DATA.SMS_COUNTER,0);
//
//        alarmHelper.setAlarm();

        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, 20000, alarmIntent);

//        Intent serviceIntent = new Intent(SmsActivity.this,SendSmsService.class);
//        startService(serviceIntent);
    }

    public void cancelSendingSms(){

        //alarmHelper.cancelAlarm();

        stopService(new Intent(SmsActivity.this,SendSmsService.class));

        //alarmManager.cancel(alarmIntent);
       // unregisterReceiver(smsBroadCast);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.fetchsms){

            fetchSms();


        }else if(v.getId()==R.id.sendsms){
            //sendSms();
          showSmsQuantityDialog();


        }else if(v.getId()==R.id.cancelsms){
            cancelSendingSms();
            sendsms.setEnabled(true);

        }else if(v.getId()==R.id.openSms){

            Intent intent = new Intent(SmsActivity.this,SmsListActivity.class);
            startActivity(intent);

        }else if(v.getId() == R.id.update_sms){

          if(isNetworkAvailable()){
              startService(new Intent(SmsActivity.this,SyncSmsService.class));
          }else builtdialog();


        }


    }

    void showSmsQuantityDialog(){

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sms_quantity_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.btn_ok);
        Button done = (Button) dialog.findViewById(R.id.btn_reload);
        final EditText editTextQuantity = (EditText) dialog.findViewById(R.id.dialogedittext);


        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String str_quantity = editTextQuantity.getText().toString();
                int quantity = Integer.parseInt(str_quantity);
                    dialog.dismiss();
                    sendsms.setEnabled(false);
                    sendSms(quantity);
            }


        });

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.y = 20;
        dialog.setCancelable(false);
        dialog.show();


    }


    void showProgressDialog(){

        progressDialog.setTitle("Fetching Sms");
        progressDialog.setMessage("Fetching Sms Please wait..");
        progressDialog.show();

    }

    @Override
    protected void onDestroy() {

        if(progressDialog.isShowing()){
            progressDialog.hide();
        }
        progressDialog =null;

        super.onDestroy();
    }

    void hideProgressDialog(){

        progressDialog.hide();

    }

    void showComletionDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Dialog dialog;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.cancel();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.cancel();

            }
        });

        builder.setTitle(message);
        dialog= builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);
    }

    public interface onMessagesComplete{

        void onSuccess(ArrayList<SmsBean> smsList);
        void onFailue(Throwable error);

    }

    private class SmsAsyncTask extends AsyncTask<Void,Void,Void>{

        ArrayList<SmsBean> smsList;

        public SmsAsyncTask(ArrayList<SmsBean> smsList){
            this.smsList= smsList;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            smsTableHelper.insertMessages(smsList);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog.isShowing()){
                progressDialog.hide();
            }
            showComletionDialog("All sms fetched Succesfully");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
                this.onBackPressed();
            return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onResume() {
        super.onResume();
        sms_info.setText("Sms info :" + "\n" + "Total Sms : "+ smsTableHelper.getAllSmsCursor(false).getCount()
                +"\n"+"Sent Sms : " + smsTableHelper.getSentSmsCursor().getCount()
                +"\n" + "Unsent Sms :" + smsTableHelper.getUnSentSmsCursor().getCount()
                +"\n" + "Permanent Failed Sms : " +smsTableHelper.getPermenantFailedSmsCursor().getCount()
                +"\n" + "Sent Not Updated :" + smsTableHelper.getSentNotUpdatedSmsCursor().getCount()
                +"\n" + "Sent And Updated :" + smsTableHelper.getLateUpdatedSmsCursor().getCount()

        );
    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
//            if(networkInfo.getState()== NetworkInfo.State.CONNECTED){
            return true;
            // }
        }

        return false;
    }

    public void builtdialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.network_dialog);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER ;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.y =  20;
        dialog.setCancelable(false);
        dialog.show();
        Button dialogok = (Button) dialog.findViewById(R.id.btn_ok);
        dialogok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button dialogreload= (Button) dialog.findViewById(R.id.btn_reload);
        dialogreload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //  Intent reload = new Intent(Splash.this,Splash.class);
                //startActivity(reload);
                //finish();

            }
        });
    }



}
