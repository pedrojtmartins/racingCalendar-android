package com.pedrojtmartins.racingcalendar.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.pedrojtmartins.racingcalendar.notifications.RCNotificationService;


public class RCAlarmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("debug", "RCAlarmBroadcastReceiver");
        int id = intent.getIntExtra("notifId", -1);
        if (id == -1) {
            // TODO: 20/04/2017 log error
            return;
        }

        Intent serviceIntent = new Intent(context, RCNotificationService.class);
        serviceIntent.putExtra("notifId", id);

        startWakefulService(context, serviceIntent);
    }
}
