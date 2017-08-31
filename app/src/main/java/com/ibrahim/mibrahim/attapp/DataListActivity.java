package com.ibrahim.mibrahim.attapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class DataListActivity extends AppCompatActivity {


    QRTableHelper qrTableHelper;
    ListView listView;
    private boolean revert=false;
    private boolean revertStatus=false;
    TextView records_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.qrlistview);
        records_info = (TextView) findViewById(R.id.records_info);

        qrTableHelper = new QRTableHelper(this);
        Cursor cursor = qrTableHelper.getAllQRCursor(true);
        getSupportActionBar().setTitle("QR List");

        if (cursor.getCount()>0) {
            ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
            listView.setAdapter(listAdapterCustom);
            showRecordsInfo();

        }else{
            showErrorDialog("No Qr Scanned Yet");

        }

    }

    void showRecordsInfo(){
        int totalCount = qrTableHelper.getAllQRCursor(false).getCount();
        int feesCount = qrTableHelper.getFeesQRCursor().getCount();
        int attendanceCount = qrTableHelper.getAttendanceQRCursor().getCount();
        int sentCount = qrTableHelper.getAllQRCursorByStatus(1).getCount();
        int unsentCount = qrTableHelper.getAllQRCursorByStatus(0).getCount();

        try {
            records_info.setText("Total Records : " + totalCount +"\t \t"+
                    "Total Fees :" + feesCount +"\n"+
                    "Total Attendance : " + attendanceCount +"\t \t"+
                    "Total Sent : "+ sentCount +"\n" +
                    "Total Unsent : " + unsentCount    );

        }catch (Exception e){
            e.printStackTrace();
        }




    }


    public Cursor getFreshCursor(){

        return qrTableHelper.getAllQRCursor(false);
    }

    void showErrorDialog(String message){

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

    void showConfirmationDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Dialog dialog;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                qrTableHelper.deleteAllRecords();
                finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_ctivity,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_revert){

            if(!revert){
                Cursor cursor = qrTableHelper.getAllQRCursor(true);
                ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
                listView.setAdapter(listAdapterCustom);
                revert = true;
            }else{
                Cursor cursor = qrTableHelper.getAllQRCursor(false);
                ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
                listView.setAdapter(listAdapterCustom);
                revert= false;
            }

        }else if(id==R.id.action_fees){

            Cursor cursor = qrTableHelper.getFeesQRCursor();
            ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
            listView.setAdapter(listAdapterCustom);


        }else if(id==R.id.action_attendance){

            Cursor cursor = qrTableHelper.getAttendanceQRCursor();
            ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
            listView.setAdapter(listAdapterCustom);


        }else if(id==R.id.action_status){

            if(!revertStatus){
                Cursor cursor = qrTableHelper.getAllQRCursorByStatus(1);
                ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
                listView.setAdapter(listAdapterCustom);
                revertStatus = true;
            }else{
                Cursor cursor = qrTableHelper.getAllQRCursorByStatus(0);
                ListAdapterCustom listAdapterCustom = new ListAdapterCustom(this,cursor);
                listView.setAdapter(listAdapterCustom);
                revertStatus= false;
            }


        }else if(id==R.id.action_delete){
         showConfirmationDialog("Confirm Delete All Records ?");

        }else if(id == android.R.id.home){

            onBackPressed();

        }

        return true;
    }



}
