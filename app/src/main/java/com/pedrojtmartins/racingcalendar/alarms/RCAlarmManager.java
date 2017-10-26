package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.RCSettings;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Pedro Martins
 * 18/04/2017
 */

public class RCAlarmManager {

    /**
     * Sets an alarm for the notification
     *
     * @param alarmManager
     * @param notification
     * @param pendingIntent
     * @return true if successful, false otherwise
     */
    public static boolean setAlarm(AlarmManager alarmManager, RCNotification notification, PendingIntent pendingIntent) {
        if (alarmManager == null || notification == null || pendingIntent == null)
            return false;

//        notification.time = "2017-06-03T15:00:00";

        Calendar calendar = DateFormatter.getCalendar(notification.time);
        if (calendar == null)
            return false;

        if (notification.time.contains("T") && notification.minutesBefore > 0) {
            calendar.add(Calendar.MINUTE, -notification.minutesBefore);
        }

        long triggerAtMillis = calendar.getTimeInMillis();
//        long now = System.currentTimeMillis();
//        long diff = triggerAtMillis - now;

//        Log.i("debug", "now (ms):   " + now);
//        Log.i("debug", "alarm (ms): " + triggerAtMillis);
//        Log.i("debug", "diff (s):   " + diff / 1000);

        if (Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        else if (Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

        return true;
    }

    /**
     * Removes a previously set alarm.
     * If the alarm was never set, nothing will happen (still returns true).
     *
     * @param alarmManager
     * @param pendingIntent
     * @return true if remove was executed, false otherwise
     */
    public static boolean removeAlarm(AlarmManager alarmManager, PendingIntent pendingIntent) {
        if (alarmManager == null || pendingIntent == null)
            return false;

        alarmManager.cancel(pendingIntent);
        return true;
    }

    public static void resetRaceAlarms(@NonNull final Context context, @NonNull final AlarmManager am, @NonNull final DatabaseManager db) {
        // Fetch all upcoming alarms
        ArrayList<RCNotification> rcNotifications = db.getUpcomingNotifications();

        if (rcNotifications == null || rcNotifications.isEmpty()) {
            // No alarms yet
            return;
        }

        FirebaseManager.logEvent(context, FirebaseManager.EVENT_ACTION_SET_NOTIFICATION_REBOOT, rcNotifications.size());

        for (RCNotification rcNotification : rcNotifications) {
            if (rcNotification.complete) {
                // Ignore completed alarms
                continue;
            }

            // Set the alarm
            final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(context, rcNotification);
            RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);
        }
    }

    public static void removePendingWeeklyNotifications(@NonNull final DatabaseManager db) {
        db.removePendingWeeklyNotifications();
    }

    public static void resetWeeklyAlarm(@NonNull final Context context, @NonNull final AlarmManager am, @NonNull final DatabaseManager db, @NonNull final RCSettings settings) {
        //Delete all pending weekly Alarms
        removePendingWeeklyNotifications(db);

        // If weekly notifications are disabled, do nothing
        if (!settings.isWeeklyNotification())
            return;

        //Figure out the correct date for the weekly notification
        String date = DateFormatter.getNextDateForDayOfWeek(settings.weeklyDayOfWeek);
        String hour = settings.weeklyHour;
        String utcDate = date + "T" + hour;

        //Build the notification, insert it in the DB and set the alarm
        RCNotification weeklyNotification = new RCNotification(utcDate);
        int newId = (int) db.addNotification(weeklyNotification);
        if (newId > 0) {
            weeklyNotification.id = newId;
            final PendingIntent pendingIntent = RCAlarmManager.generateWeeklyPendingIntent(context, weeklyNotification);
            RCAlarmManager.setAlarm(am, weeklyNotification, pendingIntent);
        } else {
            // TODO: 24/10/2017 couldn't add notif to the DB
        }
    }

    /**
     * @param date
     * @return 1 if in the future. -1 if today with no set hour. -2 if in the past
     */
    public static int isValid(String date) {
        if (date == null || date.isEmpty())
            return -3;

        if (!date.contains("T") && DateFormatter.isToday(date))
            return -1;

        if (!DateFormatter.isInTheFuture(date))
            return -2;

        return 1;
    }

    public static PendingIntent generatePendingIntent(Context context, RCNotification rcNotification) {
        Intent intent = new Intent(context, RCAlarmBroadcastReceiver.class);
        intent.putExtra("notifId", rcNotification.id);

        return PendingIntent.getBroadcast(context, rcNotification.id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }

    public static PendingIntent generateWeeklyPendingIntent(Context context, RCNotification rcNotification) {
        Intent intent = new Intent(context, RCWeeklyAlarmBroadcastReceiver.class);
        intent.putExtra("notifId", rcNotification.id);

        return PendingIntent.getBroadcast(context, rcNotification.id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }
}
