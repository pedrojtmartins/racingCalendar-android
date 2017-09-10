package com.pedrojtmartins.racingcalendar.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class RCAlarmResetBroadcastReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("debug", "RCAlarmResetBroadcastReceiver2: onReceive");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            RCAlarmResetService2.enqueueWork(context, new Intent());
        }
    }
}
