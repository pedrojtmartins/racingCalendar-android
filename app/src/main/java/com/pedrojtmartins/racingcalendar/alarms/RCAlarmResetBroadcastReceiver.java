package com.pedrojtmartins.racingcalendar.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class RCAlarmResetBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, new Intent(context, RCAlarmResetService.class));
    }
}
