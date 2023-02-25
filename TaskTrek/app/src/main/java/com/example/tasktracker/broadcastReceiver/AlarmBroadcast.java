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

import com.example.tasktracker.AddEditActivity;
import com.example.tasktracker.OnNotifyRemainderListener;
import com.example.tasktracker.R;
import com.example.tasktracker.RemainderDetailsActivity;
import com.example.tasktracker.UtilApplication;
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
        int ID = intent.getIntExtra(RemainderDetailsActivity.REMAINDER_ID, -1);
        boolean isFromRemainderDetailsActivity = intent.getBooleanExtra(RemainderDetailsActivity.IS_REMAINDER_FROM_REMAINDER_DETAILS_ACTIVITY, false);
        UtilApplication app = UtilApplication.getInstance();


        if (isFromRemainderDetailsActivity) {
            RemainderDetailsActivity activity = app.getRemainderDetailsActivity();
            listener = (OnNotifyRemainderListener) activity;
        } else {
            AddEditActivity activity = app.getAddEditActivity();
            listener = (OnNotifyRemainderListener) activity;
        }


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Intent iNotify = new Intent(context, RemainderDetailsActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, iNotify, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine(title)
                .setBigContentTitle(title);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.bell)
                    .setContentText("REMAINDER!!")
                    .setSubText("New Remainder from task tracker")
                    .setStyle(inboxStyle)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "new channel", NotificationManager.IMPORTANCE_HIGH));

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.bell)
                    .setContentText("REMAINDER!!")
                    .setSubText("New Remainder from task tracker")
                    .setStyle(inboxStyle)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build();

        }
        nm.notify(NOTIFICATION_ID, notification);

        listener.onNotifyRemainder(ID);
    }
}
