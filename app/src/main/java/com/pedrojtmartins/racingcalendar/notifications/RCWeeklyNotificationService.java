package com.pedrojtmartins.racingcalendar.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.views.activities.MainActivity;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 20/04/2017
 */

public class RCWeeklyNotificationService extends JobIntentService {
    static final int JOB_ID = 6782;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RCWeeklyNotificationService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("debug", "RCNotificationService: onHandleWork");

        final RCSettings settings = new SharedPreferencesManager(this).getSettings();
        if (!settings.isWeeklyNotification()) {
            // If weekly notifications are disabled, do nothing
            return;
        }

        int notifId = intent.getIntExtra("notifId", -1);
        if (notifId == -1) {
            // TODO: 20/04/2017 log error
            return;
        }

        final DatabaseManager db = DatabaseManager.getInstance(this);
        RCNotification rcNotification = db.getNotification(notifId);
        if (rcNotification == null || rcNotification.complete) {
            return;
        }
        db.setNotificationCompleted(rcNotification);

        // Create pending intent
        Intent touchIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, rcNotification.id, touchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Determine needed parameters
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String title = getString(R.string.weeklyUpdate);
        String msg = buildSeriesMsg(db.getRaces(true));

        // Notify
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RCNotificationManager.notify(this, pendingIntent, notificationManager, title, msg, rcNotification.id, icon, RCNotificationManager.CHANNEL_WEEKLY);

        // Set next week alarm
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        RCAlarmManager.resetWeeklyAlarm(this, am, db, settings);

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_WEEKLY_NOTIFICATION_TRIGGERED);
    }

    private String buildSeriesMsg(ArrayList<Race> upcomingFavourites) {
        StringBuilder sb = new StringBuilder();
        if (upcomingFavourites != null && !upcomingFavourites.isEmpty()) {
            sb.append(getString(R.string.weeklyUpdate));

            for (int i = 0; i < upcomingFavourites.size(); i++) {
                if (i > 0)
                    sb.append(i < upcomingFavourites.size() - 1 ? ", " : getString(R.string.and));

                sb.append(upcomingFavourites.get(i).getSeriesName());
            }
        } else {
            sb.append(getString(R.string.addFavoutiresForWeeklyNotification));
        }

        return sb.toString();
    }
}

