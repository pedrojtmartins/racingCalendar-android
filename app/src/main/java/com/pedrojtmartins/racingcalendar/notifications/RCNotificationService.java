package com.pedrojtmartins.racingcalendar.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.views.activities.MainActivity;

/**
 * Pedro Martins
 * 20/04/2017
 */

public class RCNotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int notifId = intent.getIntExtra("notifId", -1);
        if (notifId == -1) {
            // TODO: 20/04/2017 log error
            return START_NOT_STICKY;
        }

        DatabaseManager db = DatabaseManager.getInstance(this);
        RCNotification rcNotification = db.getNotification(notifId);
        if (rcNotification == null) {
            // TODO: 20/04/2017 log error
            return START_NOT_STICKY;
        }

        //Create pending intent
        Intent touchIntent = new Intent(this, MainActivity.class);
        intent.putExtra("raceId", rcNotification.raceId);
        intent.putExtra("notifId", rcNotification.id);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, rcNotification.id, touchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Determine needed parameters
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String title = rcNotification.seriesName;
        String msg = RCNotificationManager.NotificationManagerHelper.getNotificationMessage(
                getResources(),
                rcNotification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RCNotificationManager.notify(pendingIntent, builder, notificationManager, title, msg, rcNotification.id, icon);

        return START_NOT_STICKY;
    }


}
