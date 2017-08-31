package com.ibrahim.mibrahim.attapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class SmsListActivity extends AppCompatActivity {

    ListView smsList;
    SmsAdapterCustom smsAdapterCustom;
    SmsTableHelper smsTableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smsTableHelper = new SmsTableHelper(this);
        smsList = (ListView) findViewById(R.id.smsList);
        smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getAllSmsCursor(true));
        smsList.setAdapter(smsAdapterCustom);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sms_list,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId){

            case R.id.action_revert:

                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getAllSmsCursor(true));
                smsList.setAdapter(smsAdapterCustom);
                break;

            case R.id.action_sent:

                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getSentSmsCursor());
                smsList.setAdapter(smsAdapterCustom);
                break;

            case R.id.action_unsent:

                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getUnSentSmsCursor());
                smsList.setAdapter(smsAdapterCustom);
                break;

            case R.id.action_failed:
                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getPermenantFailedSmsCursor());
                smsList.setAdapter(smsAdapterCustom);
                break;


            case R.id.action_server_update:
                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getSentNotUpdatedSmsCursor());
                smsList.setAdapter(smsAdapterCustom);
                break;

            case R.id.action_delete_sms:
                smsTableHelper.deleteAllSms();
                smsAdapterCustom = new SmsAdapterCustom(this,smsTableHelper.getAllSmsCursor(false));
                smsList.setAdapter(smsAdapterCustom);
                Toast.makeText(this, "All sms deleted", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
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



}
