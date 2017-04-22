package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

        Calendar calendar = DateFormatter.getCalendar(notification.time);
        if (calendar == null)
            return false;

        if (notification.minutesBefore > 0 && notification.time.contains("T"))
            calendar.add(Calendar.MINUTE, -notification.minutesBefore);

        long timeInMillis = calendar.getTimeInMillis();
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
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
        intent.setAction(RCAlarmBroadcastReceiver.ACTION_NOTIFY);
        intent.putExtra("notifId", rcNotification.id);

        return PendingIntent.getBroadcast(context, rcNotification.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
