package com.example.task_management_app.Add;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.task_management_app.MainActivity;
import com.example.task_management_app.My_tasks.My_tasks;

import java.util.logging.Handler;


public class AlertReceiver extends BroadcastReceiver {
    String comp="non";
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences shpref=context.getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
        String ring=shpref.getString("sel_ringtone","content://settings/system/notification_sound");
        Uri ring_uri= Uri.parse(ring);
        System.out.println(ring);
        String text = intent.getStringExtra("title");
            System.out.println(text);
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            nb.setContentTitle("Task reminder")
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setSound(ring_uri);
      PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(contentIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, nb.build());
        //notificationHelper.getManager().notify(1, nb.build());

    }
}

