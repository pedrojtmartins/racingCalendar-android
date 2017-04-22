package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class RCAlarmResetService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseManager db = DatabaseManager.getInstance(this);
        ArrayList<RCNotification> rcNotifications = db.getNotifications();
        if (rcNotifications == null || rcNotifications.isEmpty()) {
            // Nothing to reset
            return START_NOT_STICKY;
        }

        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (RCNotification rcNotification : rcNotifications) {
            final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(this, rcNotification);
            RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);
        }

        return START_NOT_STICKY;
    }
}

