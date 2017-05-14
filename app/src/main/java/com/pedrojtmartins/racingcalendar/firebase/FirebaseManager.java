package com.pedrojtmartins.racingcalendar.firebase;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pedrojtmartins.racingcalendar.BuildConfig;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class FirebaseManager {
    public static final String EVENT_ACTIVITY_ABOUT = "activity_about";
    public static final String EVENT_ACTIVITY_FAVOURITES = "activity_favourites";
    public static final String EVENT_ACTIVITY_NOTIFICATIONS = "activity_notifications";
    public static final String EVENT_ACTIVITY_SETTINGS = "activity_settings";

    public static final String EVENT_ACTION_OPEN_SERIES = "action_open_series";
    public static final String EVENT_ACTION_SET_NOTIFICATION = "action_set_notification";
    public static final String EVENT_ACTION_SET_NOTIFICATION_REBOOT = "action_set_notification_reboot";
    public static final String EVENT_ACTION_SET_NOTIFICATION_TRIGGERED = "action_triggered_notification";
    public static final String EVENT_ACTION_REMOVE_NOTIFICATION = "action_remove_notification";
    public static final String EVENT_ACTION_UPDATE_NOTIFICATION = "action_update_notification";
    public static final String EVENT_ACTION_OPEN_RACE_URL = "action_open_race_url";
    public static final String EVENT_ACTION_OPEN_SERIES_URL = "action_open_series_url";


    public static boolean logEvent(Context context, String event) {
        if (BuildConfig.DEBUG)
            return true;

        if (context == null || event == null)
            return false;

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent(event, null);
        return true;
    }

    public static boolean logEvent(Context context, String event, int count) {
        if (BuildConfig.DEBUG)
            return true;

        if (context == null || event == null || count < 1)
            return false;

        for (int i = 0; i < count; i++) {
            logEvent(context, event);
        }

        return true;
    }


}
