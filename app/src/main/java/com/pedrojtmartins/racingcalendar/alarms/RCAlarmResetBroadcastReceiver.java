package com.pedrojtmartins.racingcalendar.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class RCAlarmResetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "RCAlarmResetBroadcastReceiver: onReceive");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            RCAlarmResetService.enqueueWork(context, intent);
        }
    }
}
