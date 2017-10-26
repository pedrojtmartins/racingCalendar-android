package com.pedrojtmartins.racingcalendar.notifications;

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
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.views.activities.MainActivity;

/**
 * Pedro Martins
 * 20/04/2017
 */

public class RCNotificationService extends JobIntentService {
    static final int JOB_ID = 6782;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RCNotificationService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("debug", "RCNotificationService: onHandleWork");

        int notifId = intent.getIntExtra("notifId", -1);
        if (notifId == -1) {
            // TODO: 20/04/2017 log error
            return;
        }

        DatabaseManager db = DatabaseManager.getInstance(this);
        RCNotification rcNotification = db.getNotification(notifId);
        if (rcNotification == null || rcNotification.complete) {
            //It wasn't found or it was already triggered (user restarted phone?)
            // TODO: 20/04/2017 log error
            return;
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
        String msg = RCNotificationManager.getNotificationMessage(
                getResources(),
                rcNotification);

        NotificationManager notificationManager
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RCNotificationManager.notify(this, pendingIntent, notificationManager, title, msg, rcNotification.id, icon, RCNotificationManager.CHANNEL_EVENT_STARTING);

        db.setNotificationCompleted(rcNotification);
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_NOTIFICATION_TRIGGERED);
    }
}
