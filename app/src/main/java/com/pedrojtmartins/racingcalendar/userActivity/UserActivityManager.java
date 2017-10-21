package com.pedrojtmartins.racingcalendar.userActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Settings;
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
    public static UserActivityManager getInstance() {
        if (manager == null)
            manager = new UserActivityManager();

        return manager;
    }

    private UserActivityManager() {
    }

    public int addAppStart(@NonNull final DatabaseManager db) {
        return addUserActivity(db, false, false);
    }
    private int addUserActivity(@NonNull final DatabaseManager db, boolean isRequest, boolean isAccepted) {
        String now = DateFormatter.getFormattedNow();
        return db.addUserActivity(now, isRequest, isAccepted);
    }

    public boolean isReadyToRequestRate(@NonNull final DatabaseManager db) {
        ArrayList<UserActivity> list = db.getUserActivity();
        if (list == null || list.size() < Settings.RATE_REQUEST_MINIMUM_STARTS) {
            return false;
        }

        for (UserActivity ua : list) {
            if (ua.isRequest) {
                return false;
            }
        }

        return true;
    }

    public boolean requestRate(@NonNull final Context context) {
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

        return true;
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
