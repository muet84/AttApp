package com.ibrahim.mibrahim.attapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	static String dbname = "QRDataBase";
	static int DbVersion = 4;
	Context context;
    //Table QR

    public static final String TABLE_QR = "tbl_Qr";
    public static final String COLUMN_QR_ID = BaseColumns._ID ;
    public static final String COLUMN_QR_DATA = "QR_data";
    public static final String COLUMN_QR_TYPE = "scan_type";
    public static final String COLUMN_QR_RECORDED_ID = "recorded_id";
    public static final String COLUMN_QR_TIMESTAMP = "recored_timestamp";
    public static final String COLUMN_QR_DEVICE_ID = "device_key";
    public static final String COLUMN_QR_USER_ID = "user_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_NOTE = "note";

    //Table SMS

    public static final String TABLE_SMS = "tbl_sms";
    public static final String COLUMN_SMS_ID = BaseColumns._ID ;
    public static final String COLUMN_SERVER_ID = "server_id" ;
    public static final String COLUMN_SCHOOL_ID = "school_id";
    public static final String COLUMN_PARENT_ID = "parent_id";
    public static final String COLUMN_PARENT_TYPE = "parent_type";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_MEDIUM = "medium";
    public static final String COLUMN_TO_NUMBER = "to_number";
    public static final String COLUMN_FROM_TEXT = "from_text";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENTS = "contents";
    public static final String COLUMN_SMS_STATUS = "status";
    public static final String COLUMN_CREATED_BY = "created_by";
    public static final String COLUMN_UPDATED_BY = "updated_by";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT= "updated_at";




    public DataBaseHelper(Context context) {
		super(context, dbname, null, DbVersion);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

        String querySms  = "CREATE TABLE IF NOT EXISTS `" + TABLE_SMS + "` (\n" +
                "\t`" +COLUMN_SMS_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`" + COLUMN_SERVER_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_SCHOOL_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_PARENT_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_PARENT_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_MEDIUM + "`\tTEXT,\n" +
                "\t`" + COLUMN_TO_NUMBER + "`\tTEXT,\n" +
                "\t`" + COLUMN_FROM_TEXT + "`\tTEXT,\n" +
                "\t`" + COLUMN_TITLE + "`\tINTEGER,\n" +
                "\t`" + COLUMN_CONTENTS + "`\tINTEGER,\n" +
                "\t`" + COLUMN_SMS_STATUS + "`\tTEXT,\n" +
                "\t`" + COLUMN_CREATED_BY + "`\tTEXT,\n" +
                "\t`" + COLUMN_UPDATED_BY + "`\tTEXT,\n" +
                "\t`" + COLUMN_CREATED_AT + "`\tTEXT,\n" +
                "\t`" + COLUMN_UPDATED_AT + "`\tTExt\n" +

                ");";


        String queryQR  = "CREATE TABLE IF NOT EXISTS `" + TABLE_QR + "` (\n" +
                "\t`" +COLUMN_QR_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`" + COLUMN_QR_DATA + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_RECORDED_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_QR_TIMESTAMP + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_DEVICE_ID + "`\tTEXT,\n" +
                "\t`" + COLUMN_STATUS + "`\tINTEGER,\n" +
                "\t`" + COLUMN_QR_USER_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_COMMENT + "`\tTEXT,\n" +
                "\t`" + COLUMN_NOTE + "`\tTExt\n" +



                ");";
        try {
            Log.i("query",queryQR);
			db.execSQL(queryQR);
            Log.i("query",querySms);
            db.execSQL(querySms);
		Toast.makeText(context,"Table Created", Toast.LENGTH_LONG).show();

	}
		catch(Exception e){
		Toast.makeText(context,"Can't Create", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.i("exception",e.getMessage());
			
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

        String querySms  = "CREATE TABLE IF NOT EXISTS `" + TABLE_SMS + "` (\n" +
                "\t`" +COLUMN_SMS_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`" + COLUMN_SERVER_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_SCHOOL_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_PARENT_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_PARENT_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_MEDIUM + "`\tTEXT,\n" +
                "\t`" + COLUMN_TO_NUMBER + "`\tTEXT,\n" +
                "\t`" + COLUMN_FROM_TEXT + "`\tTEXT,\n" +
                "\t`" + COLUMN_TITLE + "`\tINTEGER,\n" +
                "\t`" + COLUMN_CONTENTS + "`\tINTEGER,\n" +
                "\t`" + COLUMN_SMS_STATUS + "`\tTEXT,\n" +
                "\t`" + COLUMN_CREATED_BY + "`\tTEXT,\n" +
                "\t`" + COLUMN_UPDATED_BY + "`\tTEXT,\n" +
                "\t`" + COLUMN_CREATED_AT + "`\tTEXT,\n" +
                "\t`" + COLUMN_UPDATED_AT + "`\tTExt\n" +

                ");";


        String queryQR  = "CREATE TABLE IF NOT EXISTS `" + TABLE_QR + "` (\n" +
                "\t`" +COLUMN_QR_ID + "`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`" + COLUMN_QR_DATA + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_TYPE + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_RECORDED_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_QR_TIMESTAMP + "`\tTEXT,\n" +
                "\t`" + COLUMN_QR_DEVICE_ID + "`\tTEXT,\n" +
                "\t`" + COLUMN_STATUS + "`\tINTEGER,\n" +
                "\t`" + COLUMN_QR_USER_ID + "`\tINTEGER,\n" +
                "\t`" + COLUMN_COMMENT + "`\tTEXT,\n" +
                "\t`" + COLUMN_NOTE + "`\tTExt\n" +
                ");";
        try {
            Log.i("query",queryQR);
            db.execSQL(queryQR);
            Log.i("query",querySms);
            db.execSQL(querySms);
            Toast.makeText(context,"Database Upgraded", Toast.LENGTH_LONG).show();

        }
        catch(Exception e){
            Toast.makeText(context,"Can't Create", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.i("exception",e.getMessage());

        }

    }

    private static DataBaseHelper instance = null;

    public static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        } else if (!instance.getWritableDatabase().isOpen()) {
            instance = new DataBaseHelper(context);
        }

        return instance;
    }
}
