package com.ibrahim.mibrahim.attapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by M.Ibrahim on 4/9/2017.
 */

public class SmsTableHelper {

    Context context;
    private SQLiteDatabase db;
    AlarmHelper alarmHelper;
    ArrayList<SmsBean> sentSmsList;
    SharedPrefrencesHelper sharedPrefrencesHelper;

    public SmsTableHelper(Context context){

        this.context = context;
        db = DataBaseHelper.getInstance(context).getWritableDatabase();
        alarmHelper = AlarmHelper.getInstance(context);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(context);
    }

    public boolean insertMessages(ArrayList<SmsBean> smsList){

        long id=0;
        ContentValues cv = new ContentValues();
        Cursor cursor;

        for (SmsBean sms:smsList) {

            String queryCheck = "Select * from "+DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SERVER_ID+" = "+sms.getServer_id();
            Log.i("query",queryCheck);
            cursor = db.rawQuery(queryCheck,null);
            String smsNumTo = sms.getTo_number();

            if(cursor.getCount()<1) {

                if(!smsNumTo.equals("") && smsNumTo!=null ){
                    if(smsNumTo.length()>2){
                        if(smsNumTo.charAt(0)=='+'){
                            //do nothing
                        }else{

                            if(smsNumTo.charAt(0)=='9'){
                                smsNumTo = "+"+smsNumTo;
                            }
                            else if(smsNumTo.charAt(0)=='0'){
                                String temp="";

                                if(smsNumTo.charAt(1)=='0'){
                                    temp = smsNumTo.split("00")[1];
                                }else {
                                    temp = smsNumTo.split("0")[1];
                                }
                                smsNumTo = "+92"+temp;

                                }else if(smsNumTo.charAt(0)=='3'){
                                    smsNumTo = "+92"+smsNumTo;
                                }
                        }
                    }
                }
                cv.put(DataBaseHelper.COLUMN_SERVER_ID, sms.getServer_id());
                cv.put(DataBaseHelper.COLUMN_SCHOOL_ID, sms.getSchool_id());
                cv.put(DataBaseHelper.COLUMN_PARENT_ID, sms.getSchool_id());
                cv.put(DataBaseHelper.COLUMN_PARENT_TYPE, sms.getParent_type());
                cv.put(DataBaseHelper.COLUMN_TYPE, sms.getType());
                cv.put(DataBaseHelper.COLUMN_MEDIUM, sms.getMedium());
                cv.put(DataBaseHelper.COLUMN_TO_NUMBER, smsNumTo);
                cv.put(DataBaseHelper.COLUMN_FROM_TEXT, sms.getFrom_text());
                cv.put(DataBaseHelper.COLUMN_TITLE, sms.getTitle());
                cv.put(DataBaseHelper.COLUMN_CONTENTS, sms.getContents());
                cv.put(DataBaseHelper.COLUMN_SMS_STATUS, sms.getStatus());
                cv.put(DataBaseHelper.COLUMN_CREATED_BY, sms.getCreated_by());
                cv.put(DataBaseHelper.COLUMN_UPDATED_BY, sms.getUpdated_by());
                cv.put(DataBaseHelper.COLUMN_CREATED_AT, sms.getCreated_at());
                cv.put(DataBaseHelper.COLUMN_UPDATED_AT, sms.getUpdated_at());

                if(!db.inTransaction()){

                    id =db.insert(DataBaseHelper.TABLE_SMS,null,cv);
                }

            }

            cursor.close();

        }
        DATA.SMS_QUANTITY = 200;
        sharedPrefrencesHelper.setInt(DATA.STRING_SMS_QUANTITY,200);
        context.startService(new Intent(context,SendSmsService.class));


        return true;

    }

    public ArrayList<SmsBean> getSentSmsList() {
        Cursor c;
        SmsBean sms = null;
        sentSmsList = new ArrayList<>();

        String query = "select * from " + DataBaseHelper.TABLE_SMS + " where " +DataBaseHelper.COLUMN_SMS_STATUS +" <>15 ";
        Log.i("query", query);

        if (!db.inTransaction()) {
            c = db.rawQuery(query, null);

            while(c.moveToNext()) {
                sms = new SmsBean();

                sms.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_ID)) + "");
                sms.setServer_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SERVER_ID)) + "");
                sms.setSchool_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SCHOOL_ID)) + "");
                sms.setParent_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_ID)) + "");

                sms.setParent_type(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_TYPE)) + "");
                sms.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TYPE)) + "");
                sms.setMedium(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_MEDIUM)) + "");
                sms.setTo_number(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TO_NUMBER)) + "");

                sms.setFrom_text(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_FROM_TEXT)) + "");
                sms.setTitle(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TITLE)) + "");
                sms.setContents(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CONTENTS)) + "");
                sms.setStatus(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_STATUS)) + "");

                sms.setCreated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_BY)) + "");
                sms.setUpdated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_BY)) + "");
                sms.setCreated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_AT)) + "");
                sms.setUpdated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_AT)) + "");

                Log.i("idfetched", c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_ID)) + "");

                sentSmsList.add(sms);


                sms = null;


                //alarmHelper.cancelAlarm();


//            "\t`" +COLUMN_SMS_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
//                    "\t`" + COLUMN_SERVER_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SCHOOL_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_MEDIUM + "`\TEXT,\n" +
//                    "\t`" + COLUMN_TO_NUMBER + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_FROM_TEXT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TITLE + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_CONTENTS + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SMS_STATUS + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_AT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_AT + "`\tTExt\n" +

            }
            c.close();
        }

       return sentSmsList;

    }

    public SmsBean getSms(){

//        public static final String TABLE_SMS = "tbl_sms";
//        public static final String COLUMN_SMS_ID = BaseColumns._ID ;
//        public static final String COLUMN_SERVER_ID = "server_id" ;
//        public static final String COLUMN_SCHOOL_ID = "school_id";
//        public static final String COLUMN_PARENT_ID = "parent_id";
//        public static final String COLUMN_PARENT_TYPE = "parent_type";
//        public static final String COLUMN_TYPE = "type";
//        public static final String COLUMN_MEDIUM = "medium";
//        public static final String COLUMN_TO_NUMBER = "to_number";
//        public static final String COLUMN_FROM_TEXT = "from_text";
//        public static final String COLUMN_TITLE = "title";
//        public static final String COLUMN_CONTENTS = "contents";
//        public static final String COLUMN_SMS_STATUS = "status";
//        public static final String COLUMN_CREATED_BY = "created_by";
//        public static final String COLUMN_UPDATED_BY = "updated_by";
//        public static final String COLUMN_CREATED_AT = "created_at";
//        public static final String COLUMN_UPDATED_AT= "updated_at";
        Cursor c;
        SmsBean sms =null;

        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+ DataBaseHelper.COLUMN_SMS_STATUS+" =1 limit 1" ;
        Log.i("query",query);
        c = db.rawQuery(query, null);

        if(c.moveToFirst()){

            sms = new SmsBean();

            sms.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_ID))+"");
            sms.setServer_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SERVER_ID))+"");
            sms.setSchool_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SCHOOL_ID))+"");
            sms.setParent_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_ID))+"");

            sms.setParent_type(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_TYPE))+"");
            sms.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TYPE))+"");
            sms.setMedium(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_MEDIUM))+"");
            sms.setTo_number(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TO_NUMBER))+"");

            sms.setFrom_text(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_FROM_TEXT))+"");
            sms.setTitle(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TITLE))+"");
            sms.setContents(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CONTENTS))+"");
            sms.setStatus(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_STATUS))+"");

            sms.setCreated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_BY))+"");
            sms.setUpdated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_BY))+"");
            sms.setCreated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_AT))+"");
            sms.setUpdated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_AT))+"");



            Log.i("idfetched",c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_ID))+"");
            //alarmHelper.cancelAlarm();



//            "\t`" +COLUMN_SMS_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
//                    "\t`" + COLUMN_SERVER_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SCHOOL_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_MEDIUM + "`\TEXT,\n" +
//                    "\t`" + COLUMN_TO_NUMBER + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_FROM_TEXT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TITLE + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_CONTENTS + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SMS_STATUS + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_AT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_AT + "`\tTExt\n" +

        }
        if(c.getCount()<1){

            context.stopService(new Intent(context,SendSmsService.class));
            context.startService(new Intent(context,SyncSmsService.class));
            changeSmsStatus(-1, 1);
            Log.i("smsnull", "null sms");

            //  alarmHelper.cancelAlarm();
        }

        c.close();
    return sms;

    }

    public SmsBean getSmsById(int _id){
        Cursor c;
        SmsBean sms =null;

        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+ DataBaseHelper.COLUMN_SMS_ID+" = "+_id ;
        Log.i("query",query);
        c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            sms = new SmsBean();

            sms.setId(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_ID))+"");
            sms.setServer_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SERVER_ID))+"");
            sms.setSchool_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_SCHOOL_ID))+"");
            sms.setParent_id(c.getInt(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_ID))+"");

            sms.setParent_type(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_PARENT_TYPE))+"");
            sms.setType(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TYPE))+"");
            sms.setMedium(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_MEDIUM))+"");
            sms.setTo_number(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TO_NUMBER))+"");

            sms.setFrom_text(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_FROM_TEXT))+"");
            sms.setTitle(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_TITLE))+"");
            sms.setContents(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CONTENTS))+"");
            sms.setStatus(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_SMS_STATUS))+"");

            sms.setCreated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_BY))+"");
            sms.setUpdated_by(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_BY))+"");
            sms.setCreated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_CREATED_AT))+"");
            sms.setUpdated_at(c.getString(c.getColumnIndex(DataBaseHelper.COLUMN_UPDATED_AT))+"");


//            "\t`" +COLUMN_SMS_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
//                    "\t`" + COLUMN_SERVER_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SCHOOL_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_ID + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_PARENT_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TYPE + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_MEDIUM + "`\TEXT,\n" +
//                    "\t`" + COLUMN_TO_NUMBER + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_FROM_TEXT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_TITLE + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_CONTENTS + "`\tINTEGER,\n" +
//                    "\t`" + COLUMN_SMS_STATUS + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_BY + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_CREATED_AT + "`\tTEXT,\n" +
//                    "\t`" + COLUMN_UPDATED_AT + "`\tTExt\n" +

        }
        c.close();
        return sms;

    }

    boolean updateSmsStatus(int _id,int status){

        String query = "update "+DataBaseHelper.TABLE_SMS +  " set " +DataBaseHelper.COLUMN_SMS_STATUS+
                " = " +status+" where "+DataBaseHelper.COLUMN_SMS_ID+" = "+_id;

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

    boolean changeSmsStatus(int oldStatus,int status){

        String query = "update "+DataBaseHelper.TABLE_SMS +  " set " +DataBaseHelper.COLUMN_SMS_STATUS+
                " = " +status+" where "+DataBaseHelper.COLUMN_SMS_STATUS+" = "+oldStatus;

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

    public Cursor getAllSmsCursor(boolean reverse){

        Cursor c;
        if(reverse){
            String query = "select * from " + DataBaseHelper.TABLE_SMS+" ORDER BY "+DataBaseHelper.COLUMN_SMS_ID+" DESC ;";
            Log.i("query",query);

            c = db.rawQuery(query, null);
        }
        else{
            String query = "select * from " + DataBaseHelper.TABLE_SMS+";";
            Log.i("query",query);

            c = db.rawQuery(query, null);
        }

        return c;

    }

    public int getAllSmsCount(){

        Cursor c;
            String query = "select * from " + DataBaseHelper.TABLE_SMS+" ORDER BY "+DataBaseHelper.COLUMN_SMS_ID+" DESC ;";
            Log.i("query",query);

            c = db.rawQuery(query, null);
            int count = c.getCount();
            c.close();

        return count;

    }


    public Cursor getSentSmsCursor(){

        Cursor c;

            String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=10";
            Log.i("query",query);
            c = db.rawQuery(query, null);

        return c;

    }

    public int getSentSmsCount(){

        Cursor c;

        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=5";
        Log.i("query",query);
        c = db.rawQuery(query, null);
        int count = c.getCount();
        c.close();

        return count;

    }

    public int getUnSentSmsCount(){

        Cursor c;

        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=1";
        Log.i("query",query);
        c = db.rawQuery(query, null);
        int count = c.getCount();
        c.close();

        return count;

    }

    public Cursor getUnSentSmsCursor(){

        Cursor c;

            String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=1";
            Log.i("query",query);
            c = db.rawQuery(query, null);

        return c;

    }

    public Cursor getPermenantFailedSmsCursor(){

        Cursor c;
            String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=-10";
            Log.i("query",query);
            c = db.rawQuery(query, null);

        return c;

    }

    public Cursor getSentNotUpdatedSmsCursor(){

        Cursor c;
        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=5";
        Log.i("query",query);
        c = db.rawQuery(query, null);

        return c;

    }

    public Cursor getLateUpdatedSmsCursor(){

        Cursor c;
        String query = "select * from " + DataBaseHelper.TABLE_SMS+" where "+DataBaseHelper.COLUMN_SMS_STATUS +"=15";
        Log.i("query",query);
        c = db.rawQuery(query, null);

        return c;

    }

    void deleteAllSms(){

        String query = "delete from " + DataBaseHelper.TABLE_SMS +" where " + DataBaseHelper.COLUMN_SMS_STATUS  +" =1";
        Log.i("query",query);
        db.execSQL(query);
    }

    public void deleteSms(String id){

        String query = "delete from " + DataBaseHelper.TABLE_SMS +" WHERE " + DataBaseHelper.COLUMN_SMS_ID+"="+id;
        Log.i("query",query);

        if (!db.inTransaction()) {
            db.execSQL(query);
        }

    }




}
