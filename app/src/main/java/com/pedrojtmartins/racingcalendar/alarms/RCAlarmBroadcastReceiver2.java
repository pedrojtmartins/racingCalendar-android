package com.pedrojtmartins.racingcalendar.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.notifications.RCNotificationService;
import com.pedrojtmartins.racingcalendar.notifications.RCNotificationService2;


public class RCAlarmBroadcastReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "RCAlarmBroadcastReceiver2: onReceive");

        int id = intent.getIntExtra("notifId", -1);
        if (id == -1) {
            return;
        }

        Intent serviceIntent = new Intent(context, RCNotificationService.class);
        serviceIntent.putExtra("notifId", id);

        RCNotificationService2.enqueueWork(context, serviceIntent);
    }
}
