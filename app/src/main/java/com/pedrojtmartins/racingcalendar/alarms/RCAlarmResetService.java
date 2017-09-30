package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 10/09/2017
 */

public class RCAlarmResetService extends JobIntentService {
    static final int JOB_ID = 14782;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RCAlarmResetService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("debug", "RCAlarmResetService: onHandleWork");
        DatabaseManager db = DatabaseManager.getInstance(this);
        ArrayList<RCNotification> rcNotifications = db.getUpcomingNotifications();
        if (rcNotifications == null || rcNotifications.isEmpty()) {
            return;
        }

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_NOTIFICATION_REBOOT, rcNotifications.size());

        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (RCNotification rcNotification : rcNotifications) {
            if (rcNotification.complete)
                continue;

            final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(this, rcNotification);
            RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);
        }
    }
}
