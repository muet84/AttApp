package com.ibrahim.mibrahim.attapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by M.Ibrahim on 3/27/2017.
 */

public class SmsAdapterCustom extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
  //  QRTableHelper qrTableHelper;
    SmsTableHelper smsTableHelper;

    public SmsAdapterCustom(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        smsTableHelper = new SmsTableHelper(mContext);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.smslist_custom, parent, false);
        return v;
    }

    @Override
    public void bindView(final View v, Context context, final Cursor cursor) {

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TITLE));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_CONTENTS));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_SMS_STATUS));
        String number = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TO_NUMBER));


        TextView type_text = (TextView) v.findViewById(R.id.type);
        TextView recordedId_text = (TextView) v.findViewById(R.id.recorded_id);
        TextView type_Number = (TextView) v.findViewById(R.id.number);
        TextView status_text = (TextView) v.findViewById(R.id.status);
        Button delete = (Button) v.findViewById(R.id.delete);
        final String itemId = cursor.getString(cursor.getColumnIndex("_id"));
        delete.setText(delete.getText());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog("Delete Record ?",itemId,cursor,v);

            }
        });


        if(status.equals("1")){
            status_text.setText("Status: "+ "Unsent");
            v.setBackgroundColor(context.getResources().getColor(R.color.listBackgroundone));
        }
        else{
            status_text.setText("Status: "+ "sent");
            v.setBackgroundColor(context.getResources().getColor(R.color.listBackgroundtwo));
        }

        type_text.setText("Title: "+title);
        recordedId_text.setText("Content: "+content);
        type_Number.setText("Number: "+number);

    }

    void deleteData(){

    }

    void showDialog(String message, final String itemId, Cursor c, final View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                smsTableHelper.deleteSms(itemId);
                notifyDataSetChanged();
                v.setVisibility(View.GONE);
                mContext.startActivity(new Intent(mContext,SmsListActivity.class));
                ((Activity) mContext).finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);


    }

}
