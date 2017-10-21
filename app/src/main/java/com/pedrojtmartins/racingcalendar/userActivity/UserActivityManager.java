package com.pedrojtmartins.racingcalendar.userActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.helpers.IntentHelper;
import com.pedrojtmartins.racingcalendar.models.UserActivity;

import java.util.ArrayList;

/**
 * pedro.martins
 * 20/10/2017.
 */


public class UserActivityManager {
    private static UserActivityManager manager;
    /**
     * @return UserActivityManager instance
     */
    public static UserActivityManager getInstance() {
        if (manager == null)
            manager = new UserActivityManager();

        return manager;
    }

    private UserActivityManager() {
    }

    /**
     * Adds a new app start event into the DB
     *
     * @param db Database manager
     * @return inserted row id or -1 if an error occurred
     */
    public int addAppStart(@NonNull final DatabaseManager db) {
        return addUserActivity(db, false, false);
    }
    private int addUserActivity(@NonNull final DatabaseManager db, boolean isRequest, boolean isAccepted) {
        String now = DateFormatter.getFormattedNow();
        return db.addUserActivity(now, isRequest, isAccepted);
    }

    /**
     * Checks if all criteria are met for a rate request
     *
     * @param db Database manager
     * @return true when ready; false otherwise
     */
    public boolean isReadyToRequestRate(@NonNull final DatabaseManager db) {
        ArrayList<UserActivity> list = db.getUserActivity();
        int rateRequestMinStarts = FirebaseManager.getIntRemoteConfig(FirebaseManager.REMOTE_CONFIG_RATE_REQUEST_MIN_STARTS);
        if (list == null || list.size() < rateRequestMinStarts) {
            // Criteria has not been met yet
            return false;
        }

        for (UserActivity ua : list) {
            if (ua.isRequest) {
                //The request was already made
                return false;
            }
        }

        // We are ready to request
        return true;
    }

    /**
     * Starts the rate request process
     *
     * @param context Activity context
     */
    public void requestRate(@NonNull final Context context) {
        FirebaseManager.logEvent(context, FirebaseManager.EVENT_REQUEST_RATE);
        AlertDialogHelper.displayYesNoDialog(
                context,
                R.string.areYouHappyWithApp,
                R.string.yes,
                R.string.no,
                false,
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        if (message.what == 1) {
                            requestRatePositive(context);
                        } else {
                            requestRateNegative(context);
                        }
                        return true;
                    }
                }));
    }

    private void requestRatePositive(@NonNull final Context context) {
        FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_LIKES_APP);
        AlertDialogHelper.displayYesNoDialog(
                context,
                R.string.requestRate,
                R.string.yes,
                R.string.no,
                false,
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        requestRatePositiveResult(context, message.what);
                        return true;
                    }
                }));
    }
    private void requestRatePositiveResult(@NonNull final Context context, final int what) {
        switch (what) {
            case 1:
                FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_LIKES_Y_RATES_APP);
                addUserActivity(DatabaseManager.getInstance(context), true, true);
                IntentHelper.openGooglePlay(context);
                break;

            case 0:
                FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_LIKES_N_RATES_APP);
                addUserActivity(DatabaseManager.getInstance(context), true, false);
                break;
        }
    }

    private void requestRateNegative(@NonNull final Context context) {
        FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_DISLIKES_APP);
        AlertDialogHelper.displayYesNoDialog(
                context,
                R.string.contactForSupport,
                R.string.yes,
                R.string.no,
                false,
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        requestRateNegativeResult(context, message.what);
                        return true;
                    }
                }));
    }
    private void requestRateNegativeResult(@NonNull final Context context, int what) {
        addUserActivity(DatabaseManager.getInstance(context), true, false);

        switch (what) {
            case 1:
                FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_DISLIKES_Y_SENDS_MAIL);
                IntentHelper.sendFeedback(context);
                break;

            case 0:
                FirebaseManager.logEvent(context, FirebaseManager.EVENT_USER_DISLIKES_N_SENDS_MAIL);
                break;
        }
    }
}
