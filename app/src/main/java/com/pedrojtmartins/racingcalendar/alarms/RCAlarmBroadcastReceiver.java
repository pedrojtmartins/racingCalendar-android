package com.pedrojtmartins.racingcalendar.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.notifications.RCNotificationService;


public class RCAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "RCAlarmBroadcastReceiver: onReceive");

        int id = intent.getIntExtra("notifId", -1);
        if (id == -1) {
            return;
        }

        Intent serviceIntent = new Intent(context, RCNotificationService.class);
        serviceIntent.putExtra("notifId", id);

        RCNotificationService.enqueueWork(context, serviceIntent);
    }
}
