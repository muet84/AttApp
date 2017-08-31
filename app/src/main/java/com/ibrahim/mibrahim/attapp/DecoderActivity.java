package com.ibrahim.mibrahim.attapp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;

import java.lang.reflect.Method;

public class DecoderActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    private ViewGroup mainLayout;
    private QRCodeReaderView qrCodeReaderView;
    private PointsOverlayView pointsOverlayView;
    private QRTableHelper qrTableHelper;
    boolean isRead = false;
    Dialog dialog;
    RadioGroup radioGroupPrimary, radioGroupSecondary;
    RadioButton radiobutton_fees, radiobutton_attendacne, radiobutton_inventory,
            radiobutton_browser, radiobutton_url;
    PostData postData;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView countText;
    MediaPlayer mediaPlayer;
    String TYPE_FEES = "VOUCHER";
    String TYPE_ATTENDANCE = "ATTENDANCE";
    String TYPE_INVENTORY = "INVENTORY";
    TextView verificaionStatusText;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        init();
    }

    private boolean checkIntentForSend() {

        if (getIntent().hasExtra((DATA.USERVERIFIED))) {

            String userID = sharedPrefrencesHelper.getValue(DATA.USER_SERVER_ID, "0");
            String deviceID = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID, "0");

            //Toast.makeText(this, "" + userID + "\n" + deviceID, Toast.LENGTH_SHORT).show();


            if (userID.equals("0") && deviceID.equals("0")) {
                return false;
            } else {
                return true;
            }

        } else {
            String userID = sharedPrefrencesHelper.getValue(DATA.USER_SERVER_ID, "0");
            String deviceID = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID, "0");
            // Toast.makeText(this, "" + userID + "\n" + deviceID, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void init() {
        progressDialog = new ProgressDialog(this);
        sharedPrefrencesHelper = SharedPrefrencesHelper.getInstance(this);

        initDrawer();
        View content = getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);
        radioGroupPrimary = (RadioGroup) content.findViewById(R.id.radiogroupprimary);
        radioGroupSecondary = (RadioGroup) content.findViewById(R.id.radiogroupsecondary);
        radiobutton_fees = (RadioButton) content.findViewById(R.id.radiobutton_fees);
        radiobutton_fees.setChecked(true);
        radiobutton_attendacne = (RadioButton)content.findViewById(R.id.radiobutton_attendacne);
        radiobutton_inventory = (RadioButton) content.findViewById(R.id.radiobutton_inventory);
        radiobutton_browser = (RadioButton) content.findViewById(R.id.radiobutton_browser);
        radiobutton_url = (RadioButton) content.findViewById(R.id.radiobutton_url);
        //sharedPrefrencesHelper.setValue(SharedPrefrencesHelper.DEVICE_ID, "IBRAHIM");
        qrTableHelper = new QRTableHelper(this);
        showDebugDBAddressLogToast(this);
        mainLayout = (ViewGroup) findViewById(R.id.main_layout);
        postData = new PostData(this);
        mediaPlayer = MediaPlayer.create(this,R.raw.censor_beep);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.getBackground().setAlpha(0);

        // verificaionStatusText = (TextView)content.findViewById(R.id.verififcationstatus);
        //verificaionStatusText.setText("Device Status:");
//        if(checkFeesPermission()){
//            verificaionStatusText.setText("Verified Device");
//        }else verificaionStatusText.setText("UnVerified Device");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
            //  initCamera();

        } else {
            requestCameraPermission();
        }

        if (checkIntentForSend()) {

            if(isNetworkAvailable()){
           //     postData.syncQRTable(0);
                startService(new Intent(DecoderActivity.this,SyncQRService.class));
            }else showErrorDialog("Internet Not Available");



            // Toast.makeText(this, "Sending Now", Toast.LENGTH_SHORT).show();
        }

    }

    void initCamera(){

//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        startActivity(intent);
        getCameraInstance();

    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        mediaPlayer.start();
        processQR(text, points);
    }

    private void processQR(String text, PointF[] points) {
        qrCodeReaderView.setQRDecodingEnabled(false);
        Log.i("text", text);
        //http://schooladmin.kidsroboticacademy.com/fee-management/student-fee/print-voucher/?t=vo&id=980
        String record_id =null;

        QrDataBean qrData = new QrDataBean();
        String type = getQrCodeType();
        qrData.setId("");
        qrData.setDevice_id(sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"00"));
        qrData.setQrdata(text);
        qrData.setType(type);
        qrData.setUser_id(sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"00"));
        qrData.setTimestamp(System.currentTimeMillis() / 1000L + "");
        qrData.setStatus("0");
        qrData.setComment("Dummny Comment");
        qrData.setNote("Dummny note");


        if(type.equals(TYPE_FEES)){

            if(checkFeesPermission()){

                if (Patterns.WEB_URL.matcher(text).matches()) {
                    String splitarray[] = text.split("id=");

                    if (splitarray.length > 1) {
                        record_id = splitarray[1];
                        String openType = checkOpenType();

                        if (openType.equals(DATA.TYPE_OPEN_URL)) {
                            qrData.setNote(DATA.TYPE_OPEN_URL);
                            openURL(text);

                        } else if (openType.equals(DATA.TYPE_OPEN_BROWSER)) {
                            qrData.setNote(DATA.TYPE_OPEN_URL);

                        } else {

                        }


                    } else {
                        record_id = "notValidURL";
                    }


                } else {
                    record_id = "notValidURL";
                }


                qrData.setRecorded_id(record_id);

                showDialog(qrData);

            }else{
                showErrorDialog("You dont have permission to submit fees"+"\n" + "Please Verify FIrst");

                //Toast.makeText(this, "You dont have permission to submit fees"+"\n" + "Please Verify FIrst", Toast.LENGTH_SHORT).show();

            }

        }else {


            if (Patterns.WEB_URL.matcher(text).matches()) {
                String splitarray[] = text.split("id=");

                if (splitarray.length > 1) {
                    record_id = splitarray[1];
                    String openType = checkOpenType();

                    if (openType.equals(DATA.TYPE_OPEN_URL)) {
                        qrData.setNote(DATA.TYPE_OPEN_URL);
                        openURL(text);

                    } else if (openType.equals(DATA.TYPE_OPEN_BROWSER)) {
                        qrData.setNote(DATA.TYPE_OPEN_URL);

                    } else {

                    }


                } else {
                    record_id = "notValidURL";
                }


            } else {
                record_id = "notValidURL";
            }


            qrData.setRecorded_id(record_id);

            showDialog(qrData);
        }

    }

    public boolean checkFeesPermission(){

        String USER_PERMISSIONS =  sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS, "");
        String USER_STATUS =  sharedPrefrencesHelper.getValue(DATA.USER_STATUS, "");
        String USER_APPROVED =  sharedPrefrencesHelper.getValue(DATA.USER_APPROVED, "");
        String USER_CUSTOMER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_CUSTOMER_ID, "");
        String USER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_ID, "");

        if(!USER_PERMISSIONS.equals("")&& USER_PERMISSIONS.contains("feevoucher")&&
                !USER_STATUS.equals("")&&!USER_APPROVED.equals("")&&!USER_CUSTOMER_ID.equals("")&&
                !USER_ID.equals("")) {

            return true;

        }else{
            return false;
        }

    }

    public boolean checkAttendancePermission(){

        String USER_PERMISSIONS =  sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS, "");
        String USER_STATUS =  sharedPrefrencesHelper.getValue(DATA.USER_STATUS, "");
        String USER_APPROVED =  sharedPrefrencesHelper.getValue(DATA.USER_APPROVED, "");
        String USER_CUSTOMER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_CUSTOMER_ID, "");
        String USER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_ID, "");

        if(!USER_PERMISSIONS.equals("")&& USER_PERMISSIONS.contains("attendance")&&
                !USER_STATUS.equals("")&&!USER_APPROVED.equals("")&&!USER_CUSTOMER_ID.equals("")&&
                !USER_ID.equals("")) {

            return true;

        }else{
            return false;
        }

    }

    public boolean checkSmsPermission(){

        String USER_PERMISSIONS =  sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS, "");
        String USER_STATUS =  sharedPrefrencesHelper.getValue(DATA.USER_STATUS, "");
        String USER_APPROVED =  sharedPrefrencesHelper.getValue(DATA.USER_APPROVED, "");
        String USER_CUSTOMER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_CUSTOMER_ID, "");
        String USER_ID =  sharedPrefrencesHelper.getValue(DATA.USER_ID, "");

        if(!USER_PERMISSIONS.equals("")&& USER_PERMISSIONS.contains("message")&&
                !USER_STATUS.equals("")&&!USER_APPROVED.equals("")&&!USER_CUSTOMER_ID.equals("")&&
                !USER_ID.equals("")) {

            return true;

        }else{
            return false;
        }

    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(DecoderActivity.this, new String[]{
                            Manifest.permission.CAMERA,Manifest.permission.INTERNET}, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(mainLayout, "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void initQRCodeReaderView() {
        //Toast.makeText(this, "Camera Ready", Toast.LENGTH_SHORT).show();
        View content = getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);
        countText = (TextView)content.findViewById(R.id.count_textview);
        int count = qrTableHelper.getAllQRCursor(false).getCount();
        countText.setText("Total count :"+ count);

        qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setAutofocusInterval(1000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        // qrCodeReaderView.onPreviewFrame(null,null);

        qrCodeReaderView.startCamera();
    }

    public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Log.i("------", "ip_address: " + value);
                // Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {

            }
        }
    }

    public void showDialog(final QrDataBean qrData) {

        TextView textViewTitle;
        final EditText editTextComment;
        final CheckBox installmentCheck;

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.spalsh_dialog);
        textViewTitle = (TextView) dialog.findViewById(R.id.dialogtitle);
        editTextComment = (EditText) dialog.findViewById(R.id.dialogedittext);
        installmentCheck = (CheckBox) dialog.findViewById(R.id.installment_check);

        editTextComment.setVisibility(View.GONE);
        installmentCheck.setVisibility(View.GONE);

        textViewTitle.setText(qrData.getQrdata());

        String typ =  qrData.getType();

        if(typ.equals(TYPE_ATTENDANCE)){
            textViewTitle.setText("Reading Successfull"+"\n" + "Student" +" Id : "+qrData.getRecorded_id());
        }else  textViewTitle.setText("Reading Successfull"+"\n" + qrData.getType() +" Id : "+qrData.getRecorded_id());

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.y = 20;
        dialog.setCancelable(false);
        dialog.show();

        String type = getQrCodeType();

        if(type.equals(TYPE_FEES)){
            editTextComment.setVisibility(View.VISIBLE);
            installmentCheck.setVisibility(View.VISIBLE);
        }

        installmentCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    editTextComment.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });


        Log.i("text", type + "ID");
        qrData.setType(type);

        Button cancel = (Button) dialog.findViewById(R.id.btn_ok);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                qrCodeReaderView.setQRDecodingEnabled(true);

            }
        });
        Button done = (Button) dialog.findViewById(R.id.btn_reload);

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                qrCodeReaderView.setQRDecodingEnabled(true);

                if(installmentCheck.isChecked()){
                    String comment_text = editTextComment.getText().toString();
                    String newData = "|FEEINSTALLMENT|"+comment_text+"|";
                    qrData.setComment(newData);
                    qrData.setNote("INSTALLMENT");
                    Toast.makeText(DecoderActivity.this, "checked", Toast.LENGTH_SHORT).show();

                }else {
                    qrData.setComment(editTextComment.getText().toString());
                }


                if (authenticateUser()) {

                    long id = qrTableHelper.addQR(qrData);
                    int count = qrTableHelper.getAllQRCursor(false).getCount();
                    countText.setText("Total count :"+ count);

                    if (sharedPrefrencesHelper.checkSync()) {
                        postData.syncRecord(id);
                        // Toast.makeText(DecoderActivity.this, "Sending..", Toast.LENGTH_SHORT).show();

                    } else {
                        //Toast.makeText(DecoderActivity.this, "Sync Off..", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    long id = qrTableHelper.addQR(qrData);

                    Toast.makeText(DecoderActivity.this, "Not registered", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

        });
    }


    void showErrorDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                qrCodeReaderView.setQRDecodingEnabled(true);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                qrCodeReaderView.setQRDecodingEnabled(true);

            }
        });

        builder.setTitle(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }

        int count = qrTableHelper.getAllQRCursor(false).getCount();
        countText.setText("Total count :"+ count);


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    private String getQrCodeType() {



        radioGroupPrimary = (RadioGroup) findViewById(R.id.radiogroupprimary);
        radioGroupSecondary = (RadioGroup) findViewById(R.id.radiogroupsecondary);
        radiobutton_fees = (RadioButton) findViewById(R.id.radiobutton_fees);
        radiobutton_attendacne = (RadioButton) findViewById(R.id.radiobutton_attendacne);
        radiobutton_inventory = (RadioButton) findViewById(R.id.radiobutton_inventory);
        radiobutton_browser = (RadioButton) findViewById(R.id.radiobutton_browser);
        radiobutton_url = (RadioButton) findViewById(R.id.radiobutton_url);


        int primaryId = radioGroupPrimary.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = (RadioButton) findViewById(primaryId);

        Log.i("text", primaryId + "ID");

        if (primaryId == R.id.radiobutton_fees) {

            return TYPE_FEES;
        } else if (primaryId == R.id.radiobutton_attendacne) {


            return TYPE_ATTENDANCE;


        } else if (primaryId == R.id.radiobutton_inventory) {

            return TYPE_INVENTORY;
        } else {

            return "0";
        }
    }


    private boolean authenticateUser() {


        String device_id = sharedPrefrencesHelper.getValue(SharedPrefrencesHelper.DEVICE_ID, "false");

        if (device_id.equals("false") || device_id.equals("") || device_id.equals(null)) {

            return false;
        } else {
            //  Toast.makeText(this, "deviceId" + device_id, Toast.LENGTH_SHORT).show();
            return true;
        }

    }


    private void initDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ATTApp");


//        if(checkFeesPermission()){
//      //      verificaionStatusText.setText("Verified  Device");
//            getSupportActionBar().setTitle("ATTApp" +"\t \t \t" +"Verified Device" );
//        }else getSupportActionBar().setTitle("ATTApp" +"\t \t \t" +"UnVerified Device" );







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // qrCodeReaderView.startCamera();
                //qrCodeReaderView.setQRDecodingEnabled(true);
                //initQRCodeReaderView();
                //Toast.makeText(DecoderActivity.this, "closed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //qrCodeReaderView.stopCamera();
                // qrCodeReaderView = null;
                //   Toast.makeText(DecoderActivity.this, "opened", Toast.LENGTH_SHORT).show();


            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    void showCompletionDialog(String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dialogInterface.cancel();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dialogInterface.cancel();
            }
        });

        builder.setTitle(message);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.setTitle(message);
    }



    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_send) {

            String userId = sharedPrefrencesHelper.getValue(DATA.USER_SERVER_ID, "0");

            if (userId.equals("0")) {

                Intent gotoLogin = new Intent(DecoderActivity.this, LoginActivity.class);
                gotoLogin.putExtra(DATA.REQUEST_LOGIN, DATA.REQUEST_TYPE_SYSTEM);
                startActivity(gotoLogin);

            } else {
                if(isNetworkAvailable()){

//                    postData.syncQRTable(0);
                    startService(new Intent(DecoderActivity.this,SyncQRService.class));

                }else showErrorDialog("Internet Not Available");
                // showProgressDialog();
            }
            //  postData.syncQRTable();
        }
        //else if (id == R.id.nav_settings) {
//            Intent gotoSetting = new Intent(DecoderActivity.this, SettingActivity.class);
//            startActivity(gotoSetting);
//
        //  }
        else if (id == R.id.nav_list) {
            Intent gotoList = new Intent(DecoderActivity.this, DataListActivity.class);
            startActivity(gotoList);
        } else if (id == R.id.nav_login) {
            Intent gotoLogin = new Intent(DecoderActivity.this, LoginActivity.class);
            gotoLogin.putExtra(DATA.REQUEST_LOGIN, DATA.REQUEST_TYPE_USER);
            startActivity(gotoLogin);

        }else if(id == R.id.sms){

            if(checkSmsPermission()){
                Intent gotoSms = new Intent(DecoderActivity.this, SmsActivity.class);
                startActivity(gotoSms);
            }else showErrorDialog("Youd dont have Permission for Messages , please Contact Administrator");


        }
        else if(id == R.id.device_details){

            Intent gotoDeviceInfo = new Intent(DecoderActivity.this, DeviceInfoActivity.class);
            startActivity(gotoDeviceInfo);

        }
        else if(id == R.id.nav_settings){

            Intent gotoDeviceInfo = new Intent(DecoderActivity.this, SettingActivity.class);
            startActivity(gotoDeviceInfo);

        }

        else if(id == R.id.mark_attendance){

            if(checkAttendancePermission()){
                showMarkAttendanceDialog();
            }else showErrorDialog("You Dont Have Permission to mark Attendance. \n Please Contact Administrator");

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMarkAttendanceDialog(){

        final QrDataBean qrData = new QrDataBean();
        String type = "NONE";
        qrData.setId("");
        qrData.setDevice_id(sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"00"));
        qrData.setQrdata("mark attendance");
        qrData.setType(type);
        qrData.setUser_id(sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"00"));
        qrData.setTimestamp(System.currentTimeMillis() / 1000L + "");
        qrData.setStatus("0");
        qrData.setComment("Dummny Comment");
        qrData.setNote("Dummny note");


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mark_attendance_dialog);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER ;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.y =  20;
        dialog.setCancelable(false);
        dialog.show();

        Button dialogok = (Button) dialog.findViewById(R.id.btn_ok);
        final EditText attendanceId = (EditText) dialog.findViewById(R.id.mark_attendance_edittext);

        dialogok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                String str_attendanceId = attendanceId.getText().toString();
//                qrData.setRecorded_id(str_attendanceId);
//                qrTableHelper.addQR(qrData);
                dialog.dismiss();
            }
        });
        Button dialogreload= (Button) dialog.findViewById(R.id.btn_reload);
        dialogreload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str_attendanceId = attendanceId.getText().toString();
                qrData.setRecorded_id(str_attendanceId);
                qrTableHelper.addQR(qrData);
                Toast.makeText(DecoderActivity.this, "Attendance Marked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                            }
        });
    }




    @Override
    public void onStop() {
        super.onStop();
    }


    private String checkOpenType(){
        int primaryId = radioGroupSecondary.getCheckedRadioButtonId();
        RadioButton checkedRadioButton = (RadioButton) findViewById(primaryId);

        Log.i("text", primaryId + "ID");

        if (primaryId == R.id.radiobutton_url) {

            return DATA.TYPE_OPEN_URL;
        } else if (primaryId == R.id.radiobutton_browser) {


            return DATA.TYPE_OPEN_BROWSER;
        }
        return "";
    }
    private void openURL(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }
    private String openBrowser(){

        return null;
    }



    void showProgressDialog(){

        progressDialog.setTitle("Sending data");
        progressDialog.setMessage("Sync in progress");
        progressDialog.show();

    }

    void hideProgressDialog(){
        progressDialog.hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String str_deviceKey = sharedPrefrencesHelper.getValue(DATA.DEVICE_ID,"");
        String str_permissions = sharedPrefrencesHelper.getValue(DATA.USER_PERMISSIONS,"");

        if(!str_deviceKey.equals("") && !str_permissions.equals("")) {
            getMenuInflater().inflate(R.menu.menu_main,menu);
        }else  getMenuInflater().inflate(R.menu.menu_unverified,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
//            if(networkInfo.getState()== NetworkInfo.State.CONNECTED){
            return true;
            // }
        }

        return false;
    }

}