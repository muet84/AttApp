package com.ibrahim.mibrahim.attapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

/**
 * Created by M.Ibrahim on 5/6/2017.
 */

public class SmsNotifier {


    public SmsNotifier(){


    }

    void notify(Context context, String Title, String Content, int Image, int id,int progress,int max){

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent i = new Intent(context,SmsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,i,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context);
        Notification notification = builder
                .setContentText(Content)
                .setTicker("Ticker")
                .setContentTitle(Title)
                .setSmallIcon(Image)
                .setProgress(max,progress,false)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false

                )
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        //we can keep any id 1,0,2 any,just to differentiate b/w notifications
        notificationManager.notify(id,notification);
    }

}
