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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by M.Ibrahim on 3/27/2017.
 */

public class ListAdapterCustom extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    QRTableHelper qrTableHelper;

    public ListAdapterCustom(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        qrTableHelper = new QRTableHelper(mContext);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.qrlist_customn, parent, false);
        return v;
    }

    @Override
    public void bindView(final View v, Context context, final Cursor cursor) {

        String type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_QR_TYPE));
        String recorded_id = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_QR_RECORDED_ID));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_STATUS));
        String timeStamp  = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_QR_TIMESTAMP));

        long longTime = Long.parseLong(timeStamp);

        String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(longTime * 1000L));

        TextView type_text = (TextView) v.findViewById(R.id.type);
        TextView recordedId_text = (TextView) v.findViewById(R.id.recorded_id);
        TextView status_text = (TextView) v.findViewById(R.id.status);
        TextView time_text = (TextView) v.findViewById(R.id.timestamp);


        Button delete = (Button) v.findViewById(R.id.delete);
        final String itemId = cursor.getString(cursor.getColumnIndex("_id"));
        delete.setText(delete.getText());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog("Delete Record ?",itemId,cursor,v);

            }
        });


        if(status.equals("0")){
            status_text.setText("Status: "+ "Unsent");
            v.setBackgroundColor(context.getResources().getColor(R.color.listBackgroundone));
        }
        else{
            status_text.setText("Status: "+ "sent");
            v.setBackgroundColor(context.getResources().getColor(R.color.listBackgroundtwo));
        }

        type_text.setText("Type: "+type);
        recordedId_text.setText("Record Id: "+recorded_id);
        time_text.setText("Time :" +time);

    }

    void deleteData(){

    }

    void showDialog(String message, final String itemId, Cursor c, final View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                qrTableHelper.deleteQR(itemId);
                notifyDataSetChanged();
                v.setVisibility(View.GONE);
                mContext.startActivity(new Intent(mContext,DataListActivity.class));
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
