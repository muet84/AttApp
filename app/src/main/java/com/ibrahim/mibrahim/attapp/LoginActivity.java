package com.ibrahim.mibrahim.attapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    EditText username , password;
    Button submit,verify;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Sign In");

        //


        username = (EditText) findViewById(R.id.edit_text_username);
        password = (EditText) findViewById(R.id.edit_text_password);
        submit = (Button) findViewById(R.id.submit);
        verify = (Button) findViewById(R.id.verify);
        notice = (TextView) findViewById(R.id.notice);
        sharedPrefrencesHelper =SharedPrefrencesHelper.getInstance(this);

        String str_username = sharedPrefrencesHelper.getValue(DATA.USERNAME,"");
        String str_deviceKey = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"");
        String str_permissions = sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS,"");

        if(!str_deviceKey.equals("")){
            submit.setVisibility(View.INVISIBLE);
        }

        if(!str_permissions.equals("")){
            verify.setText("Update Permissions");
            notice.setVisibility(View.VISIBLE);

            //verify.setVisibility(View.INVISIBLE);
           // showDialog("You are Registered & Verified");

        }
//        sharedPrefrencesHelper.setValue(DATA.USERNAME,"admin");
//        sharedPrefrencesHelper.setValue(DATA.DEVICE_ID,"68b8d71939908e620499575e5ff0113f");
//        sharedPrefrencesHelper.setValue(DATA.USER_SERVER_ID,"61");



        username.setText(str_username);

        if(str_username.equals("")){

        }
        else{
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(checkRegistration()){
                verifyUser();
            }else {
                Toast.makeText(LoginActivity.this, "Please Register First", Toast.LENGTH_SHORT).show();
            }


            }
        });

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

    private boolean checkRegistration() {


        String device_id = sharedPrefrencesHelper.getValue(SharedPrefrencesHelper.DEVICE_ID, "false");

        if (device_id.equals("false") || device_id.equals("") || device_id.equals(null)) {

            return false;
        } else {
            Toast.makeText(this, "deviceId" + device_id, Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public interface onRegistrationComplete {

         void onSuccess(UserDeviceBean userDeviceBean);
         void onFailue(Throwable error);

    }

    public interface onVerificationComplete {

        void onSuccess(UserDeviceBean userDeviceBean);
        void onFailue(Throwable error);

    }


    private void registerUser(){

        PostData postData = new PostData(this);

        final UserDeviceBean userDeviceInfo = new UserDeviceBean("school-admin",getIMEI(),"Not required",getManufacture(),username.getText().toString(),
                password.getText().toString(),"server_Id",getBrand(),getYearBuild(),getOs(),getOsVersion(),"No comment","No note","deviceKey","","","","");


        postData.registerUser(userDeviceInfo, new onRegistrationComplete() {
            @Override
            public void onSuccess(UserDeviceBean userDeviceBean) {

                Intent gotomain = new Intent(LoginActivity.this,DecoderActivity.class);
                String requestType=null;
                if(getIntent().hasExtra(DATA.REQUEST_LOGIN)){
                    requestType = getIntent().getStringExtra(DATA.REQUEST_LOGIN);
                }

                sharedPrefrencesHelper.setValue(DATA.USERNAME,username.getText().toString());
                sharedPrefrencesHelper.setValue(DATA.DEVICE_ID,userDeviceBean.getDevice_ID());
                sharedPrefrencesHelper.setValue(DATA.USER_SERVER_ID,userDeviceBean.getServer_ID());


                if(requestType.equals(DATA.REQUEST_TYPE_SYSTEM)){
                    gotomain.putExtra(DATA.USERVERIFIED,true);
                    startActivity(gotomain);
                    finish();

                }else if(requestType.equals(DATA.REQUEST_TYPE_USER)){
                    startActivity(gotomain);
                    finish();
                }


            }

            @Override
            public void onFailue(Throwable error) {
                Toast.makeText(LoginActivity.this, "Cannot Verify", Toast.LENGTH_SHORT).show();
                Log.i("verficationError",error.getMessage());


                Intent gotomain = new Intent(LoginActivity.this,DecoderActivity.class);
                String requestType=null;
                if(getIntent().hasExtra(DATA.REQUEST_LOGIN)){
                    requestType = getIntent().getStringExtra(DATA.REQUEST_LOGIN);
                }

                //sharedPrefrencesHelper.setValue(DATA.DEVICE_ID,"UnVerifiedDevice");
                sharedPrefrencesHelper.setValue(DATA.USER_SERVER_ID,"UnVerifiedUser");
               // sharedPrefrencesHelper.setValue(DATA.USERNAME,username.getText().toString());

                if(requestType.equals(DATA.REQUEST_TYPE_SYSTEM)){
                    gotomain.putExtra(DATA.USERVERIFIED,true);
                    startActivity(gotomain);
                    finish();

                }else if(requestType.equals(DATA.REQUEST_TYPE_USER)){
                    startActivity(gotomain);
                    finish();
                }

            }
        });

    }

    private void verifyUser(){

        PostData postData = new PostData(this);
        postData.verifyUser(new onVerificationComplete() {
            @Override
            public void onSuccess(UserDeviceBean userDeviceBean) {

                if (userDeviceBean.getPermissions()!=null && !userDeviceBean.getPermissions().equals("")&&!userDeviceBean.getPermissions().equals("null")&&
                userDeviceBean.getStatus()!=null && !userDeviceBean.getStatus().equals("")&& !userDeviceBean.getStatus().equals("null")&&
                userDeviceBean.getApproved()!=null && !userDeviceBean.getApproved().equals("")&& !userDeviceBean.getApproved().equals("null")&&
                userDeviceBean.getCustomer_id()!=null && !userDeviceBean.getCustomer_id().equals("")&& !userDeviceBean.getCustomer_id().equals("null")&&
                userDeviceBean.getUser_id()!=null && !userDeviceBean.getUser_id().equals("")&& !userDeviceBean.getUser_id().equals("null")) {


                    sharedPrefrencesHelper.setValue(DATA.USER_PERMISSIONS, userDeviceBean.getPermissions());
                    sharedPrefrencesHelper.setValue(DATA.USER_STATUS, userDeviceBean.getStatus());
                    sharedPrefrencesHelper.setValue(DATA.USER_APPROVED, userDeviceBean.getApproved());
                    sharedPrefrencesHelper.setValue(DATA.USER_CUSTOMER_ID, userDeviceBean.getCustomer_id());
                    sharedPrefrencesHelper.setValue(DATA.USER_ID, userDeviceBean.getUser_id());
                    sharedPrefrencesHelper.setValue(DATA.SERVER_ID, userDeviceBean.getServer_ID());

                }
                else{
                    Toast.makeText(LoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailue(Throwable error) {
                Toast.makeText(LoginActivity.this, "Error Occured : Failed TO Verify", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public String getIMEI(){

        TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.i("IMEI",mngr.getDeviceId());

        return  mngr.getDeviceId();
    }
    public String getManufacture(){

        String manufacturer = Build.MANUFACTURER;
        Log.i("manufacture",manufacturer);

        return manufacturer;
    }
    public String getYearBuild(){

        String IMEI = Build.ID;
        Log.i("IMEI",IMEI);

        return IMEI;
    }public String getBrand(){

        String IMEI = Build.BRAND;
        Log.i("IMEI",IMEI);

        return IMEI;
    }public String getOs(){

        String IMEI = Build.VERSION.RELEASE;
        Log.i("IMEI",IMEI);

        return IMEI;
    }public String getOsVersion(){

        String version = Build.VERSION.RELEASE;
        Log.i("IMEI",version);

        return version;
    }




}
