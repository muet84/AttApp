package com.ibrahim.mibrahim.attapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by M.Ibrahim on 3/25/2017.
 */

public class QRTableHelper  {

    Context context;
    private SQLiteDatabase db;
    long remainingTime ;

    public QRTableHelper(Context context){
        this.context = context;
        this.db = DataBaseHelper.getInstance(context).getWritableDatabase();

    }

    public long addQR(QrDataBean qrData){

        long id = 0;

        boolean validate = validateQrTime(qrData);

        if(!validate){

            showQrScannedDialog("This QR code is already scanned is this hour !" + "\n" + "Try Again after "+remainingTime + " Minutes");

        }else {

            ContentValues cv = new ContentValues();
            cv.put(DataBaseHelper.COLUMN_QR_DATA, qrData.getQrdata());
            cv.put(DataBaseHelper.COLUMN_QR_TYPE, qrData.getType());
            cv.put(DataBaseHelper.COLUMN_QR_RECORDED_ID, qrData.getRecorded_id());
            cv.put(DataBaseHelper.COLUMN_QR_TIMESTAMP, qrData.getTimestamp());
            cv.put(DataBaseHelper.COLUMN_QR_DEVICE_ID, qrData.getDevice_id());
            cv.put(DataBaseHelper.COLUMN_QR_USER_ID, qrData.getUser_id());
            cv.put(DataBaseHelper.COLUMN_STATUS, Integer.parseInt(qrData.getStatus()));
            cv.put(DataBaseHelper.COLUMN_COMMENT, qrData.getComment());
            cv.put(DataBaseHelper.COLUMN_NOTE, qrData.getNote());


            if (!db.inTransaction()) {
                id = db.insert(DataBaseHelper.TABLE_QR, null, cv);

            }
        }
        return id;
    }

    public void updateQR(QrDataBean qrData){

    }

    public void deleteQR(String id){

        String query = "delete from " + DataBaseHelper.TABLE_QR +" WHERE " + DataBaseHelper.COLUMN_QR_ID+"="+id;
        Log.i("query",query);

        if (!db.inTransaction()) {
            db.execSQL(query);
        }

    }

    public ArrayList<QrDataBean> getUnsentQRList(){

            Cursor c;
            String query = "select * from " + DataBaseHelper.TABLE_QR +" WHERE " + DataBaseHelper.COLUMN_STATUS+"=0";
            Log.i("query",query);
            ArrayList<QrDataBean> QRDataList = new ArrayList<>();
            QrDataBean qrDataBean;

            Log.i("ComplainQuery",query);

            if (!db.inTransaction()) {
                c = db.rawQuery(query, null);

                while(c.moveToNext()){
                    qrDataBean = new QrDataBean();

                    qrDataBean.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_ID))+"");
                    qrDataBean.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TYPE)));
                    qrDataBean.setQrdata(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DATA)));
                    qrDataBean.setTimestamp(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TIMESTAMP))+"");
                    qrDataBean.setRecorded_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_RECORDED_ID))+"");
                    qrDataBean.setDevice_id(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DEVICE_ID))+"");
                    qrDataBean.setUser_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_USER_ID))+"");
                    qrDataBean.setStatus(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_STATUS))+"");
                    qrDataBean.setComment(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_COMMENT))+"");
                    qrDataBean.setNote(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_NOTE))+"");


                    QRDataList.add(qrDataBean);
                    qrDataBean = null;
                }
                c.close();
            }
             return QRDataList;
        }

    public ArrayList<QrDataBean> getAllQRList(){
        Cursor c;
        String query = "select * from " + DataBaseHelper.TABLE_QR +";";
        Log.i("query",query);
        ArrayList<QrDataBean> QRDataList = new ArrayList<>();
        QrDataBean qrDataBean;

        Log.i("ComplainQuery",query);

        if (!db.inTransaction()) {
            c = db.rawQuery(query, null);

            while(c.moveToNext()){
                qrDataBean = new QrDataBean();
                qrDataBean.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_ID))+"");
                qrDataBean.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TYPE)));
                qrDataBean.setQrdata(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DATA)));
                qrDataBean.setTimestamp(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TIMESTAMP))+"");
                qrDataBean.setRecorded_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_RECORDED_ID))+"");
                qrDataBean.setDevice_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DEVICE_ID))+"");
                qrDataBean.setUser_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_USER_ID))+"");
                qrDataBean.setStatus(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_STATUS))+"");


                QRDataList.add(qrDataBean);
                qrDataBean = null;
            }
            c.close();
        }
        return QRDataList;

    }

    public Cursor getAllQRCursor(boolean reverse){

        Cursor c;
        if(reverse){
            String query = "select * from " + DataBaseHelper.TABLE_QR+" ORDER BY "+DataBaseHelper.COLUMN_QR_ID+" DESC ;";
            Log.i("query",query);

            c = db.rawQuery(query, null);
        }
        else{
            String query = "select * from " + DataBaseHelper.TABLE_QR+";";
            Log.i("query",query);

            c = db.rawQuery(query, null);
        }



        return c;

    }

    public QrDataBean getRecordByID(int id){
        Cursor c;
        String query = "select * from " + DataBaseHelper.TABLE_QR +" WHERE " + DataBaseHelper.COLUMN_QR_ID+"= "+id;

        Log.i("query",query);
        QrDataBean qrDataBean = new QrDataBean();

        Log.i("Query",query);

        if (!db.inTransaction()) {
            c = db.rawQuery(query, null);

            if (c.moveToFirst()){

                qrDataBean.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_ID))+"");
                qrDataBean.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TYPE)));
                qrDataBean.setQrdata(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DATA)));
                qrDataBean.setTimestamp(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TIMESTAMP))+"");
                qrDataBean.setRecorded_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_RECORDED_ID))+"");
                qrDataBean.setDevice_id(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_DEVICE_ID))+"");
                qrDataBean.setUser_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_QR_USER_ID))+"");
                qrDataBean.setStatus(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_STATUS))+"");
                qrDataBean.setComment(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_COMMENT))+"");
                qrDataBean.setNote(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_NOTE))+"");

            }
            c.close();
        }
        return qrDataBean;

    }

    boolean updateStatus(int _id,int status){

            String query = "update "+DataBaseHelper.TABLE_QR +  " set " +DataBaseHelper.COLUMN_STATUS+
                    " = " +status+" where "+DataBaseHelper.COLUMN_QR_ID+" = "+_id;

            try {
               // db.rawQuery(query, null);
                db.execSQL(query);
                Log.i("quert",query);


            }
            catch(Exception e){
                e.printStackTrace();
                return false;

            }
            return true;

        }

//    public Cursor getUnsentQRCursor(boolean reverse){
//
//        Cursor c;
//        if(reverse){
//            String query = "select * from " + DataBaseHelper.TABLE_QR+" ORDER BY "+DataBaseHelper.COLUMN_QR_ID+" DESC ;";
//            Log.i("query",query);
//
//            c = db.rawQuery(query, null);
//        }
//        else{
//            String query = "select * from " + DataBaseHelper.TABLE_QR+";";
//            Log.i("query",query);
//
//            c = db.rawQuery(query, null);
//        }
//
//
//
//        return c;
//
//    }

//    public Cursor getSentQRCursor(){
//
//        Cursor c;
//
//            String query = "select * from " + DataBaseHelper.TABLE_QR+" ORDER BY "+DataBaseHelper.COLUMN_QR_ID+" DESC ;";
//            Log.i("query",query);
//
//            c = db.rawQuery(query, null);
//
//            return c;
//
//    }

    public Cursor getAttendanceQRCursor(){

        Cursor c;

            String query = "select * from " + DataBaseHelper.TABLE_QR+" where "+ DataBaseHelper.COLUMN_QR_TYPE +" = " + "'"+ DATA.TYPE_ATTENDANCE+"'";
            Log.i("query",query);

            c = db.rawQuery(query, null);

        return c;

    }

    public Cursor getFeesQRCursor(){

        Cursor c;

            String query = "select * from " + DataBaseHelper.TABLE_QR+" where "+ DataBaseHelper.COLUMN_QR_TYPE +" = " +"'"+ DATA.TYPE_FEES+"';";
            Log.i("query",query);

            c = db.rawQuery(query, null);

        return c;

    }

    public Cursor getAllQRCursorByStatus(int status){

        Cursor c;

            String query = "select * from " + DataBaseHelper.TABLE_QR+" where " + DataBaseHelper.COLUMN_STATUS +" = " +status+";";
            Log.i("query",query);

            c = db.rawQuery(query, null);


        return c;

    }

    void deleteAllRecords(){

        String query = "delete from " + DataBaseHelper.TABLE_QR;
        Log.i("query",query);
        db.execSQL(query);
    }

    boolean validateQrTime(QrDataBean qrData){

        Cursor c;

        String query = "select * from " + DataBaseHelper.TABLE_QR+" where "+
                DataBaseHelper.COLUMN_QR_RECORDED_ID +" = " + "'"+ qrData.getRecorded_id()+"'"+" ORDER BY "+DataBaseHelper.COLUMN_QR_ID+" DESC ;";

        Log.i("query",query);

        c = db.rawQuery(query, null);
        if(c.getCount()>0){

            if(c.moveToFirst()){

                String timeStamp = qrData.getTimestamp();
                String previousTimeStamp = c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_QR_TIMESTAMP));

                long firstTime = Long.parseLong(timeStamp);
                long secondTime = Long.parseLong(previousTimeStamp);

                long directdiff = firstTime-secondTime;

                long diffInMinn = TimeUnit.SECONDS.toSeconds(directdiff);
                long timeInMins = diffInMinn/60;
                Log.i("time1", diffInMinn+ "\n" + timeInMins);


                if(timeInMins>60){
                    return true;
                }else {
                    remainingTime  = 60-timeInMins;
                    return false;
                }
//                String firstDate = getDate(firstTime);
//                String secondDate = getDate(secondTime);
//
//                //String diff = firstDate.get
//               // new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(1494790966 * 1000L))
//
//                try {
//                    Date time1 = new SimpleDateFormat("dd-MM-yyyy").parse(firstDate);
//                    Date time2 = new SimpleDateFormat("dd-MM-yyyy").parse(secondDate);
//
//                    long diffInM = time1.getTime() -  time2.getTime();
//
//
//                    long diffInMin = TimeUnit.MINUTES.toSeconds(diffInM);
//
//
//                    Toast.makeText(context, ""+diffInMin, Toast.LENGTH_SHORT).show();
//                    Log.i("time1", diffInMin+"");
//
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                CharSequence result = DateUtils.getRelativeTimeSpanString(secondTime, firstTime, 0);
//
//
//                Log.i("time2", result+"");
//                Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();

            }

        }else return true;

        return false;
    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }



    void showQrScannedDialog(String message){

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

        builder.setTitle("QR scanned");
        builder.setMessage(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
    }


}



