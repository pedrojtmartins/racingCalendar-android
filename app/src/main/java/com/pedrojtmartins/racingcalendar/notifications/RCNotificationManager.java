package com.pedrojtmartins.racingcalendar.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import static android.support.v4.app.NotificationCompat.DEFAULT_LIGHTS;
import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

/**
 * Pedro Martins
 * 18/04/2017
 */

public class RCNotificationManager {
    public static boolean notify(final PendingIntent pendingIntent,
                                 final NotificationCompat.Builder builder,
                                 final android.app.NotificationManager notificationManager,
                                 final String title,
                                 final int id,
                                 final Bitmap icon) {
        if (builder == null || pendingIntent == null || notificationManager == null || id <= 0 || icon == null)
            return false;


        Notification notification = builder
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE | DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setLargeIcon(icon)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        notificationManager.notify(id, notification);

        return true;
    }


    static class NotificationManagerHelper {
        static String getNotificationTitle(Resources resources, String seriesName, RCNotification notification) {
            if (resources == null || seriesName == null || notification == null)
                return "";

            String title;
            if (notification.time.contains("T")) {
                if (notification.minutesBefore > 0) {
                    title = String.format(
                            resources.getString(R.string.notifRaceBefore),
                            seriesName, notification.minutesBefore);
                } else {
                    title = String.format(resources.getString(R.string.notifRace), seriesName);
                }
            } else {
                title = String.format(resources.getString(R.string.notifRaceToday), seriesName);
            }
            return title;
        }
    }
}
