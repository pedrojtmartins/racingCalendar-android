package com.pedrojtmartins.racingcalendar.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

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
    public static final String CHANNEL_EVENT_STARTING = "channel_event_starting";
    public static final String CHANNEL_WEEKLY = "channel_weekly";

    static boolean notify(final Context context,
                          final PendingIntent pendingIntent,
                          final android.app.NotificationManager notificationManager,
                          final String title,
                          final String msg,
                          final int id,
                          final Bitmap icon,
                          final String channel) {
        if (context == null || pendingIntent == null || notificationManager == null || id <= 0 || icon == null)
            return false;

        Notification notification = new NotificationCompat.Builder(context, channel)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE | DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(icon)
                .setSmallIcon(R.mipmap.ic_launcher_notification)
                .build();

        notificationManager.notify(id, notification);
        return true;
    }

    static String getNotificationMessage(Resources resources, RCNotification notification) {
        if (resources == null || notification == null)
            return "";

        String title;
        if (notification.time.contains("T")) {
            if (notification.minutesBefore > 0) {
                title = String.format(resources.getString(R.string.notifRaceBefore), notification.minutesBefore);
            } else {
                title = resources.getString(R.string.notifRace);
            }
        } else {
            title = resources.getString(R.string.notifRaceToday);
        }
        return title;
    }


    public static void createChannels(NotificationManager mNotificationManager, Resources resources) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        CharSequence name = resources.getString(R.string.notification_channel_race_starting);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_EVENT_STARTING, name, importance);
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setLightColor(Color.RED);
        mChannel.setVibrationPattern(new long[]{500, 250, 250, 250, 500});
        mNotificationManager.createNotificationChannel(mChannel);

        name = resources.getString(R.string.notification_channel_weekly);
        importance = NotificationManager.IMPORTANCE_DEFAULT;

        mChannel = new NotificationChannel(CHANNEL_WEEKLY, name, importance);
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setLightColor(Color.RED);
        mChannel.setVibrationPattern(new long[]{500, 250, 250, 250, 500});
        mNotificationManager.createNotificationChannel(mChannel);
    }
}
