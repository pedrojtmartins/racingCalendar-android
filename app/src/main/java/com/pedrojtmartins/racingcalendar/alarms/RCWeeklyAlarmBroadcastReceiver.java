package com.pedrojtmartins.racingcalendar.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.notifications.RCWeeklyNotificationService;


public class RCWeeklyAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "RCWeeklyAlarmBroadcastReceiver: onReceive");
        RCWeeklyNotificationService.enqueueWork(context, intent);
    }
}
