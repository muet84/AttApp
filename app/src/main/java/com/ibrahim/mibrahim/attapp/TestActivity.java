package com.ibrahim.mibrahim.attapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {

    QRTableHelper qrTableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        qrTableHelper = new QRTableHelper(this);

       String qrText = getIntent().getStringExtra("qrtext");

        QrDataBean qrData = new QrDataBean();

        qrData.setId("");
        qrData.setDevice_id("DevieID");
        qrData.setQrdata(qrText);
        qrData.setRecorded_id("RecordedId");
        qrData.setType("Type");
        qrData.setUser_id("userId");
        qrData.setTimestamp(System.currentTimeMillis() / 1000L+"");

        qrTableHelper.addQR(qrData);


    }
}
