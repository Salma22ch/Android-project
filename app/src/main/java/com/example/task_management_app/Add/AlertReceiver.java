package com.example.task_management_app.Add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.util.logging.Handler;


public class AlertReceiver extends BroadcastReceiver {
    String comp="non";
    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("title");
            System.out.println(text);
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            nb.setContentTitle("Task reminder").setContentText(text);
            notificationHelper.getManager().notify(1, nb.build());

    }
}

