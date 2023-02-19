package com.example.tasktracker.broadcastReceiver;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.tasktracker.OnNotifyRemainderListener;
import com.example.tasktracker.R;
import com.example.tasktracker.RemainderDetailsActivity;
import com.example.tasktracker.entities.Remainder;

import java.io.Serializable;

public class AlarmBroadcast extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 1;

    private OnNotifyRemainderListener listener;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onReceive(Context context, Intent intent) {


        String title = intent.getStringExtra(RemainderDetailsActivity.REMAINDER_TITLE);
        int ID = intent.getIntExtra(RemainderDetailsActivity.REMAINDER_ID,-1);
        if (RemainderDetailsActivity.onNotifyRemainderListener != null){
            listener = RemainderDetailsActivity.onNotifyRemainderListener;
        }
        Toast.makeText(context, "IDdddd: " +ID, Toast.LENGTH_SHORT).show();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Intent iNotify = new Intent(context,RemainderDetailsActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, iNotify, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine(title)
                .setBigContentTitle(title);
        Toast.makeText(context, "step 1", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.bell)
                    .setContentText("REMAINDER!!")
                    .setSubText("New Remainder from task tracker")
                    .setStyle(inboxStyle)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"new channel",NotificationManager.IMPORTANCE_HIGH));
            Toast.makeText(context, "step 2", Toast.LENGTH_SHORT).show();
        }
        else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.bell)
                    .setContentText("REMAINDER!!")
                    .setSubText("New Remainder from task tracker")
                    .setStyle(inboxStyle)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            Toast.makeText(context, "step 3", Toast.LENGTH_SHORT).show();
        }
        nm.notify(NOTIFICATION_ID,notification);
        Toast.makeText(context, "step 4", Toast.LENGTH_SHORT).show();
        try{
            listener.onNotifyRemainder(ID);
            Toast.makeText(context, "step 6", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
