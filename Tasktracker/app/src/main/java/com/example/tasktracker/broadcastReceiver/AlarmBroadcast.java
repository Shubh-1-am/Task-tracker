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

import com.example.tasktracker.R;
import com.example.tasktracker.RemainderDetailsActivity;

public class AlarmBroadcast extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 1;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onReceive(Context context, Intent intent) {


//
//        Intent notificationIntent = new Intent(context, RemainderDetailsActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Toast.makeText(context, "Step 1", Toast.LENGTH_SHORT).show();
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Task Tracker")
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
////                .setCustomContentView(notificationLayout)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        Toast.makeText(context, "Step 2", Toast.LENGTH_SHORT).show();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "My Notification Channel";
//            String description = "My notification channel description";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            channel.enableVibration(true);
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            Toast.makeText(context, "Step 3", Toast.LENGTH_SHORT).show();
//        }
//
//         notificationManager = NotificationManagerCompat.from(context);
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//            // Permission is already granted
//            notificationManager.notify(NOTIFICATION_ID, builder.build());
//            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
//        } else {
//            // Permission is not yet granted
//            Toast.makeText(context, "Requesting p", Toast.LENGTH_SHORT).show();
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 5);
//            Toast.makeText(context, "Requesting Permission", Toast.LENGTH_SHORT).show();
//        }
//
//        Toast.makeText(context, "Broadcast Receiver got Triggered", Toast.LENGTH_LONG).show();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            String title = intent.getStringExtra("title");
//
//            RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//            notificationLayout.setTextViewText(R.id.notification_title, title);
//            notificationLayout.setImageViewResource(R.id.notification_image, R.mipmap.ic_launcher_round);
//
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "NOTIFICATION_TITLE",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            serviceChannel.enableVibration(true);
//            serviceChannel.enableLights(true);
//            serviceChannel.setShowBadge(true);
//
//            NotificationManager manager = context.getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//
//            Intent contentIntent = new Intent(context, RemainderDetailsActivity.class);
//            PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_MUTABLE);
//
//            Intent fullScreenIntent = new Intent(context, RemainderDetailsActivity.class);
//            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_MUTABLE);
//
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
//
//            notificationBuilder.setAutoCancel(true)
//                    .setSmallIcon(R.mipmap.ic_launcher_round)
//                    .setOngoing(true)
//                    .setOnlyAlertOnce(true)
//                    .setContentTitle("NOTIFICATION_TITLE")
//                    .setContent(notificationLayout)
//                    .setContentIntent(contentPendingIntent)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setCategory(NotificationCompat.CATEGORY_ALARM)
//                    .setFullScreenIntent(fullScreenPendingIntent, true)
//                    .setChannelId(CHANNEL_ID);
//
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1, notificationBuilder.build());
//
//        }

        String title = intent.getStringExtra("title");
        Toast.makeText(context, ""+title, Toast.LENGTH_SHORT).show();

//        Intent intent1 = new Intent(context, RemainderDetailsActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"notify_001");
//
//        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        notificationLayout.setTextViewText(R.id.notification_title, title);
//        notificationLayout.setImageViewResource(R.id.notification_image, R.mipmap.ic_launcher_round);
//
//        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        notificationLayout.setOnClickPendingIntent(R.id.notification_snooze_button, pendingSwitchIntent);
//        mBuilder.setSmallIcon(R.drawable.clock);
//        mBuilder.setAutoCancel(true);
//        mBuilder.setOngoing(true);
//        mBuilder.setPriority(Notification.PRIORITY_HIGH);
//        mBuilder.setOnlyAlertOnce(true);
//        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
//        mBuilder.setContent(notificationLayout);
//        mBuilder.setContentIntent(pendingIntent);
//
//        Toast.makeText(context, "Step 1", Toast.LENGTH_SHORT).show();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "Your_channel_id";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel name",
//                    NotificationManager.IMPORTANCE_HIGH);
//            channel.enableVibration(true);
//            notificationManager.createNotificationChannel(channel);
//            mBuilder.setChannelId(channelId);
//            Toast.makeText(context, "Step 2", Toast.LENGTH_SHORT).show();
//        }
//
//        Notification notification = mBuilder.build();
//        notificationManager.notify(1, notification);
//        Toast.makeText(context, "finish 1", Toast.LENGTH_SHORT).show();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Intent iNotify = new Intent(context,RemainderDetailsActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, iNotify, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine(title)
                .setBigContentTitle(title);


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
        }
        nm.notify(NOTIFICATION_ID,notification);



    }
}
