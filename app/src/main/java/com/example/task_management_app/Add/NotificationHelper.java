package com.example.task_management_app.Add;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.task_management_app.R;

public class NotificationHelper extends ContextWrapper {
    Boolean vib_mode;
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private NotificationManager mManager;
    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        SharedPreferences shpref=getApplicationContext().getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
        vib_mode = shpref.getBoolean("vib_mode",false);
        if(vib_mode) channel.setVibrationPattern(new long[] { 0 , 500 });
        else channel.setVibrationPattern(new long[] {0});
        channel.enableVibration(true);
        //channel.setSound(null, null);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification() {

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_notifications);
    }
}
