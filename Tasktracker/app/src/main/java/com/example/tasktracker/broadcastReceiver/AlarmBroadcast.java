package com.example.tasktracker.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String date_time = bundle.getString("date");
    }
}
