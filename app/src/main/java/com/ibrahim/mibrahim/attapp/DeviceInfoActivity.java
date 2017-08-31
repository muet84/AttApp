package com.ibrahim.mibrahim.attapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceInfoActivity extends AppCompatActivity {

    SharedPrefrencesHelper sharedPrefrencesHelper;
    ListView deviceInfoList;
    ArrayList<String>deviceInfo;
    ArrayAdapter<String>deviceInfoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(this);
        deviceInfoList = (ListView) findViewById(R.id.device_info_list);
        deviceInfo = new ArrayList<>();


        String str_username = sharedPrefrencesHelper.getValue(DATA.USERNAME,"");
        String str_deviceKey = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"");
        String str_permissions = sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS,"");

        if(!str_deviceKey.equals("") && !str_permissions.equals("")){

            deviceInfo.add("Device ID : "+sharedPrefrencesHelper.getValue(DATA.SERVER_ID,""));
            deviceInfo.add("Device Key :" + sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,""));
            deviceInfo.add("User Name : "+str_username);
            deviceInfo.add("User Permissions : "+sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS,""));
            deviceInfo.add("User Status : "+sharedPrefrencesHelper.getValue(DATA.USER_STATUS,""));
            deviceInfo.add("User Approved : "+sharedPrefrencesHelper.getValue(DATA.USER_APPROVED,""));
            deviceInfo.add("User Customer Id : "+sharedPrefrencesHelper.getValue(DATA.USER_CUSTOMER_ID,""));
            deviceInfo.add("User ID : "+sharedPrefrencesHelper.getValue(DATA.USER_ID,""));

            deviceInfoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,deviceInfo);
            deviceInfoList.setAdapter(deviceInfoAdapter);

        }   else Toast.makeText(this, "Sorry Your device is not registered", Toast.LENGTH_SHORT).show();


    }


}
