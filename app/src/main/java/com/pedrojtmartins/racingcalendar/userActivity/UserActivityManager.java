package com.pedrojtmartins.racingcalendar.userActivity;

import com.pedrojtmartins.racingcalendar._settings.Settings;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.models.UserActivity;

import java.util.ArrayList;

/**
 * pedro.martins
 * 20/10/2017.
 */

public class UserActivityManager {
    public static boolean isReadyToRequestRate(DatabaseManager db) {
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

    public static int addAppStart(DatabaseManager db) {
        return addUserActivity(db, false, false);
    }

    public static int addUserActivity(DatabaseManager db, boolean isRequest, boolean isAccepted) {
        String now = DateFormatter.getFormattedNow();
        return db.addUserActivity(now, isRequest, isAccepted);
    }
}
