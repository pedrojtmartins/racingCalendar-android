package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import java.util.Calendar;

/**
 * Pedro Martins
 * 18/04/2017
 */

public class RCAlarmManager {

    /**
     * Sets an alarm for the target time
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
        long now = System.currentTimeMillis();
        long diff = triggerAtMillis - now;

        Log.i("debug", "now (ms):   " + now);
        Log.i("debug", "alarm (ms): " + triggerAtMillis);
        Log.i("debug", "diff (s):   " + diff / 1000);

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
}
