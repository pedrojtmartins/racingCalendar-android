package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;

import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pedro on 18/04/2017.
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

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
}
