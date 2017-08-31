package com.ibrahim.mibrahim.attapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import  org.apache.http.entity.StringEntity;

/**
 * Created by M.Ibrahim on 3/25/2017.
 */

public class PostData {

    Context context;
    QRTableHelper qrTableHelper;
    SmsTableHelper smsTableHelper;
    int count=0;
    int sms_count=0;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    ArrayList<QrDataBean> qrList =null;
    ArrayList<SmsBean> smsList = null;
    ProgressDialog pd;
    SmsNotifier smsNotifier;
    AsyncHttpClient smsSyncclient = new AsyncHttpClient();
    StringEntity smsSyncEntity = null;
    JSONObject smsSyncjsonParams = new JSONObject();
    String deviceKey ;
    String smsSyncurl="";

    public  PostData(Context context){

        this.context = context;
        qrTableHelper = new QRTableHelper(context);
        smsTableHelper = new SmsTableHelper(context);
        sharedPrefrencesHelper =SharedPrefrencesHelper.getInstance(context);
        pd = new ProgressDialog(context);
        smsNotifier = new SmsNotifier();
        deviceKey =sharedPrefrencesHelper.getValue(DATA.DEVICE_ID, "default");
    }

    public void syncRecord(long id){

        if(!isNetworkAvailable()){
            builtdialog();
        }else {


            final int QRid = (int) id;
            Log.i("error", QRid + "");
            QrDataBean qrDataBean = qrTableHelper.getRecordByID(QRid);
            Log.i("error", qrDataBean.getId() + "");
            //JSONArray jsonArray = new JSONArray();
            StringEntity entity = null;
            JSONObject jsonParams = new JSONObject();

            try {
                jsonParams.put("index_from_device", qrDataBean.getId());
                jsonParams.put(DataBaseHelper.COLUMN_QR_DATA, qrDataBean.getQrdata());
                jsonParams.put(DataBaseHelper.COLUMN_QR_TYPE, qrDataBean.getType());
                jsonParams.put("relative_id", qrDataBean.getRecorded_id());
                jsonParams.put(DataBaseHelper.COLUMN_QR_TIMESTAMP, qrDataBean.getTimestamp());
                jsonParams.put(DataBaseHelper.COLUMN_QR_DEVICE_ID, qrDataBean.getDevice_id());
                jsonParams.put(DataBaseHelper.COLUMN_QR_USER_ID, qrDataBean.getUser_id());
                jsonParams.put(DataBaseHelper.COLUMN_STATUS, qrDataBean.getStatus());
                jsonParams.put(DataBaseHelper.COLUMN_COMMENT, qrDataBean.getComment());
                jsonParams.put(DataBaseHelper.COLUMN_NOTE, qrDataBean.getNote());


                //jsonArray.put(smsSyncjsonParams);
                entity = new StringEntity(jsonParams.toString());
                printData(entity);

            } catch (Exception e) {

                e.printStackTrace();
                Log.i("error", e.getMessage());
            }
            //Log.i("sendSingle",qrDataBean.getId());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(1000000);
            client.post(context, DATA.BASE_URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                    Log.i("response", "" + response);

                    try {
                        JSONObject responseobj = new JSONObject(response);

                        if (responseobj.has("id")) {
                            boolean isInserted = qrTableHelper.updateStatus(QRid, 1);
                            if (isInserted) {
                                Toast.makeText(context, "Record Sent and updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("response", "" + error.getMessage() + "\n" + new String(responseBody));

                }
            });
        }

    }

    public void syncQRTable(final int id){

        if(!isNetworkAvailable()){

        //    builtdialog();

            smsNotifier.notify(context, "Internet Connection Failed", "Service Stopped",
                    R.drawable.ic_verified_user_white_24dp, 2, 100, 100);
            context.stopService(new Intent(context,SyncQRService.class));

        }else {

            if(qrList==null){
                qrList = qrTableHelper.getUnsentQRList();
            }

           // Toast.makeText(context, "Send All", Toast.LENGTH_SHORT).show();


            // JSONArray jsonArray = new JSONArray();
            StringEntity entity = null;
            if (id < qrList.size()) {

                JSONObject jsonParams = new JSONObject();

                QrDataBean qrDataBean = qrList.get(id);

                try {
                    jsonParams.put("index_from_device", qrDataBean.getId());
                    jsonParams.put(DataBaseHelper.COLUMN_QR_DATA, qrDataBean.getQrdata());
                    jsonParams.put(DataBaseHelper.COLUMN_QR_TYPE, qrDataBean.getType());
                    jsonParams.put("relative_id", qrDataBean.getRecorded_id());
                    jsonParams.put(DataBaseHelper.COLUMN_QR_TIMESTAMP, qrDataBean.getTimestamp());
                    jsonParams.put(DataBaseHelper.COLUMN_QR_DEVICE_ID, qrDataBean.getDevice_id());
                    //smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_USER_ID, qrDataBean.getUser_id());
                    jsonParams.put(DataBaseHelper.COLUMN_QR_USER_ID, sharedPrefrencesHelper.getValue(DATA.USER_ID, ""));
                    jsonParams.put(DataBaseHelper.COLUMN_STATUS, qrDataBean.getStatus());
                    jsonParams.put(DataBaseHelper.COLUMN_COMMENT, qrDataBean.getComment());
                    jsonParams.put(DataBaseHelper.COLUMN_NOTE, qrDataBean.getNote());
                    jsonParams.put(DATA.SERVER_ID, sharedPrefrencesHelper.getValue(DATA.SERVER_ID, ""));
                    jsonParams.put("scan_by", "QRCode");

//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_ID, qrDataBean.getId());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_DATA, qrDataBean.getQrdata());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_TYPE, qrDataBean.getType());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_RECORDED_ID, qrDataBean.getRecorded_id());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_TIMESTAMP, qrDataBean.getTimestamp());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_DEVICE_ID, qrDataBean.getDevice_id());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_QR_USER_ID, qrDataBean.getUser_id());
//                smsSyncjsonParams.put(DataBaseHelper.COLUMN_STATUS, qrDataBean.getStatus());

//                jsonArray.put(smsSyncjsonParams);

                    entity = new StringEntity(jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                context.stopService(new Intent(context,SyncQRService.class));
                smsNotifier.notify(context,"All QR update Successfully","Server Update Done",
                        R.drawable.ic_verified_user_white_24dp,2,id,qrList.size());


//                ((DecoderActivity) context).hideProgressDialog();
//                ((DecoderActivity)context).showCompletionDialog("Data Sent Succesfully");

                return;
            }
            if (entity != null) {
                printData(entity);

                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(100000);
                client.post(context, DATA.BASE_URL, entity, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String respose = new String(responseBody);
                        //Toast.makeText(context, "" + respose, Toast.LENGTH_SHORT).show();
                        Log.i("response", "" + respose + "\n" + DATA.BASE_URL);

                        try {
                            JSONObject jsonObject = new JSONObject(respose);
                            if (jsonObject.has("id")) {
                                boolean isInserted = qrTableHelper.updateStatus(Integer.parseInt(qrList.get(id).getId()), 1);
                                if (isInserted) {
                                    count++;
                                    syncQRTable(count);
                                    smsNotifier.notify(context,"Updating QR To Server","Internet Should Remain Active",
                                            R.drawable.ic_verified_user_white_24dp,2,id,qrList.size());

                                } else {
                                    count++;
                                    syncQRTable(count);
                                    //syncQRTable(id);
                                }

                            }
                        } catch (JSONException error) {

                            count++;
                            syncQRTable(count);
                            //syncQRTable(id);
                            Toast.makeText(context, "Record Not sent , Error occured", Toast.LENGTH_SHORT).show();
                            Log.i("responseIn exception", "" + error.getMessage() + "\n" + error.getMessage() + "\n" + DATA.BASE_URL);

                            error.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    String respose = new String(responseBody);
                        if(responseBody!=null){
                            Log.i("onFailureResponseBodyQR",new String(responseBody));
                        }

                        Log.i("onFailureQR",statusCode +"\n" + headers +"\n" + error.getMessage() + "\n" + error.getCause());


                        smsNotifier.notify(context,"Error While Updating QR","",
                                R.drawable.ic_verified_user_white_24dp,2,id,qrList.size());

                        Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("responseIn Failure", "" + error.getMessage() + "\n" + error.getMessage() + "\n" + DATA.BASE_URL);
                        count++;
                        syncQRTable(count);




                        //syncQRTable(id);
                    }
                });

            }else{

                context.stopService(new Intent(context,SyncQRService.class));
                smsNotifier.notify(context,"All QR update Successfully","Server Update Done",
                        R.drawable.ic_verified_user_white_24dp,2,id,qrList.size());

//                ((DecoderActivity)context).hideProgressDialog();
//                ((DecoderActivity)context).showCompletionDialog("Data Sent Succesfully");
            }
        }

    }

    public void printData(StringEntity entity){
        try {
            InputStream inputStream = entity.getContent();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            Log.i("entity",total.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyUser(final LoginActivity.onVerificationComplete
            verificationCompleteCallback){

        if(!isNetworkAvailable()){
            builtdialog();
            return false;

          }else {

            AsyncHttpClient verificationclient = new AsyncHttpClient();
            verificationclient.setTimeout(10000);
            final String user_server_id = sharedPrefrencesHelper.getValue(DATA.USER_SERVER_ID, "false");
            final String url = DATA.DEVICE_VERFICATION_URL+"id="+user_server_id;

            verificationclient.get(url, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(String content) {
                   // String response = new String(responseBody);
                    UserDeviceBean userDeviceResponseInfo = null;

                    Log.i("response", content+"\n"+url);
                    try {
                        JSONObject mainObject = new JSONObject(content);

                        String application = mainObject.getString("application");
                        String imei_number = mainObject.getString("imei_number");

                        String permissions = mainObject.getString("permissions");

                        String manufacturer = mainObject.getString("manufacturer");
                        String brand = mainObject.getString("brand");
                        String os = mainObject.getString("os");
                        String os_version = mainObject.getString("os_version");
                        String year_build = mainObject.getString("year_build");
                        String serverID = mainObject.getString("id");
                        String deviceID = mainObject.getString("device_key");
                        String status = mainObject.getString("status");
                        String approved = mainObject.getString("approved");
                        String customer_id = mainObject.getString("customer_id");
                        String user_id = mainObject.getString("user_id");

                        userDeviceResponseInfo = new UserDeviceBean(application, imei_number, permissions,
                                manufacturer, "", "",serverID, deviceID, os, os_version, "", "", "", "",status,approved,customer_id,user_id);
                        verificationCompleteCallback.onSuccess(userDeviceResponseInfo);

                    } catch (JSONException e) {
                        Log.i("exception",e.getMessage());
                        e.printStackTrace();
                        verificationCompleteCallback.onFailue(e);

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    verificationCompleteCallback.onFailue(error);
                    Log.i("response", "failure"+error.getMessage()+"\n"+url);
                }
            });

        }
        return false;
    }


    public boolean registerUser(UserDeviceBean userdeviceInfo, final LoginActivity.onRegistrationComplete
            registrationCompleteCallback){

        if(!isNetworkAvailable()){
            builtdialog();
            return false;
        }else {


            AsyncHttpClient registrationclient = new AsyncHttpClient();
            JSONObject jsonParams = new JSONObject();
            StringEntity entity = null;
            try {
                jsonParams.put("application", userdeviceInfo.getApplication());
                jsonParams.put("imei_number", userdeviceInfo.getImeiNumber());
                jsonParams.put("permissions", userdeviceInfo.getPermissions());
                jsonParams.put("manufacturer", userdeviceInfo.getManufacturer());
                jsonParams.put("brand", userdeviceInfo.getBrand());
                jsonParams.put("year_build", userdeviceInfo.getYear_build());
                jsonParams.put("os", userdeviceInfo.getOS());
                jsonParams.put("os_version", userdeviceInfo.getOS_version());
                jsonParams.put("comment", userdeviceInfo.getComment());
                jsonParams.put("note", userdeviceInfo.getNote());

                entity = new StringEntity(jsonParams.toString());
                Log.i("Registering","registerdata"+userdeviceInfo.getImeiNumber());
                printData(entity);

            } catch (Exception e) {
                e.printStackTrace();

            }


//        RequestParams params = new RequestParams();
//        params.put("username",username);
//        params.put("",password);
//        params.put("",IMEI);

            registrationclient.post(context, DATA.DEVICE_REG_URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    UserDeviceBean userDeviceResponseInfo = null;

                    Log.i("response", response);
                    try {
                        JSONObject mainObject = new JSONObject(response);
                        String application = mainObject.getString("application");
                        String imei_number = mainObject.getString("imei_number");
                        String permissions = mainObject.getString("permissions");
                        String manufacturer = mainObject.getString("manufacturer");
                        String brand = mainObject.getString("brand");
                        String os = mainObject.getString("os");
                        String os_version = mainObject.getString("os_version");
//                    String comment = mainObject.getString("comment");
//                    String note = mainObject.getString("note");
                        String year_build = mainObject.getString("year_build");
                        String serverID = mainObject.getString("id");
                        String deviceID = mainObject.getString("device_key");

                        userDeviceResponseInfo = new UserDeviceBean(application, imei_number, permissions,
                                manufacturer, "", "", serverID, deviceID, brand, year_build, os, os_version, "comment", "note","","","","");
                        registrationCompleteCallback.onSuccess(userDeviceResponseInfo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        registrationCompleteCallback.onFailue(e);

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    registrationCompleteCallback.onFailue(error);
                    Log.i("response", error.getMessage() + "\n");
                }
            });

        }
        return false;
    }



    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
//            if(networkInfo.getState()== NetworkInfo.State.CONNECTED){
                return true;
           // }
        }

        return false;
    }

    public void builtdialog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.spalsh_dialog);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM ;
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

    public void updateSmsToServer(int _id){

        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        Log.i("updatesms","server");
        SmsBean sms = smsTableHelper.getSmsById(_id);

        try {
            jsonParams.put("id",sms.getServer_id()+"");
            jsonParams.put("school_id",sms.getSchool_id());
            jsonParams.put("parent_id",sms.getParent_id());
            jsonParams.put("parent_type",sms.getParent_type());
            jsonParams.put("type",sms.getType());
            jsonParams.put("medium",sms.getMedium());
            jsonParams.put("to_number",sms.getTo_number());
            jsonParams.put("from_text",sms.getFrom_text());
            jsonParams.put("title",sms.getTitle());
            jsonParams.put("contents",sms.getContents());
            jsonParams.put("status",10);
            jsonParams.put("created_by",sms.getCreated_by());
            jsonParams.put("updated_by",sms.getUpdated_by());
            jsonParams.put("created_at",sms.getCreated_at());
            jsonParams.put("updated_at",sms.getUpdated_at());

            entity = new StringEntity(jsonParams.toString());
            printData(entity);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       // http://schoolapi.kidsroboticacademy.com/api/v1/notification/1?device_key=15de21c670ae7c3f6f3f1f37029303c9
        String deviceKey = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"default");
        String URL = DATA.SMS_UPDATE_URL+sms.getServer_id()+"?"+"device_key="+deviceKey;
        client.put(context,URL,entity,"application/json",new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);

                if(responseBody!=null) {
                                 String response = new String(responseBody);
                                 Log.i("smsResponse",response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
//                String response = new String(responseBody);
//                Log.i("smsResponse",response);

                if(responseBody!=null) {
                    String response = new String(responseBody);
                    Log.i("smsResponse",response + "\n" + "Error Message" +error.getMessage());
                }

            }
        });

    }

    public void syncSentMessages(final int id){

        Log.i("smsupdateID",id+"");

        if(!isNetworkAvailable()){

        //    builtdialog();

            smsNotifier.notify(context, "Internet Connection Failed", "Service Stopped",
                    R.drawable.ic_verified_user_white_24dp, 1, 100, 100);
            context.stopService(new Intent(context,SyncSmsService.class));

        }else {

            if (smsList == null) {
                smsList = smsTableHelper.getSentSmsList();
            }

            Log.i("smsupdateList",smsList.size()+"");


            smsSyncclient.setTimeout(100000);


            Log.i("updatesms", "server");
            if (id < smsList.size()) {

                smsNotifier.notify(context,"Updating Sms To Server","Internet Should Remain Active",
                        R.drawable.ic_verified_user_white_24dp,1,id,smsList.size());

               // showProgressDialog("Updating messages...");
                final SmsBean sms = smsList.get(id);

                try {
                    smsSyncjsonParams.put("id", sms.getServer_id() + "");
                    smsSyncjsonParams.put("school_id", sms.getSchool_id());
                    smsSyncjsonParams.put("parent_id", sms.getParent_id());
                    smsSyncjsonParams.put("parent_type", sms.getParent_type());
                    smsSyncjsonParams.put("type", sms.getType());
                    smsSyncjsonParams.put("medium", sms.getMedium());
                    smsSyncjsonParams.put("to_number", sms.getTo_number());
                    smsSyncjsonParams.put("from_text", sms.getFrom_text());
                    smsSyncjsonParams.put("title", sms.getTitle());
                    smsSyncjsonParams.put("contents", sms.getContents());
                    smsSyncjsonParams.put("status", sms.getStatus());
                    smsSyncjsonParams.put("created_by", sms.getCreated_by());
                    smsSyncjsonParams.put("updated_by", sms.getUpdated_by());
                    smsSyncjsonParams.put("created_at", sms.getCreated_at());
                    smsSyncjsonParams.put("updated_at", sms.getUpdated_at());

                    smsSyncEntity = new StringEntity(smsSyncjsonParams.toString());
                    printData(smsSyncEntity);

                } catch (JSONException e) {
                    sms_count++;
                    syncSentMessages(sms_count);
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    sms_count++;
                    syncSentMessages(sms_count);
                    e.printStackTrace();
                }
                // http://schoolapi.kidsroboticacademy.com/api/v1/notification/1?device_key=15de21c670ae7c3f6f3f1f37029303c9
                smsSyncurl = DATA.SMS_UPDATE_URL + sms.getServer_id() + "?" + "device_key=" + deviceKey;

                smsSyncclient.put(context, smsSyncurl, smsSyncEntity, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);

                        try {
                        if (responseBody != null) {
                            String response = new String(responseBody);
                            Log.i("smsResponse", response);
                            JSONObject respObject = null;

                                respObject = new JSONObject(response);
                                if(respObject.has("status")){

                                    smsTableHelper.updateSmsStatus(Integer.parseInt(sms.getId()),15);
                                    sms_count++;
                                    syncSentMessages(sms_count);

                                }else {
                                    sms_count++;
                                    syncSentMessages(sms_count);
                                }

                        } else {
                            sms_count++;
                            syncSentMessages(sms_count);
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sms_count++;
                            syncSentMessages(sms_count);

                        }

                        }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        super.onFailure(statusCode, headers, responseBody, error);
//                String response = new String(responseBody);
//                Log.i("smsResponse",response);

                       // hideProgressDialog();
//                        showCompletionDialog("Error Occured while Updating");
                      //  Toast.makeText(context, "Error Occured while Updating", Toast.LENGTH_SHORT).show();

                        if (responseBody != null) {
                            String response = new String(responseBody);
                            Log.i("smsResponse", response + "\n" + "Error Message" + error.getMessage());
                        }else{ Log.i("error",error.getMessage());
                        }

                        sms_count++;
                        syncSentMessages(sms_count);

                    }
                });

            }else{
                smsNotifier.notify(context,"Sms Sent And Updated Succesfully","",R.drawable.ic_verified_user_white_24dp,1,100,100);
                context.stopService(new Intent(context,SyncSmsService.class));



                // hideProgressDialog();
                //showCompletionDialog("Sms updated succesfully");


            }
        }

    }

    void showCompletionDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setTitle(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);
    }

    void showProgressDialog(String message){
        pd.setMessage(message);
        pd.show();
    }

    void hideProgressDialog(){

        pd.hide();

    }


    private HashMap<String, String> convertHeadersToHashMap(Header[] headers) {
        HashMap<String, String> result = new HashMap<String, String>(headers.length);
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return result;
    }
}
