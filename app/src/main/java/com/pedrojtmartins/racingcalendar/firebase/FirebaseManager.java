package com.pedrojtmartins.racingcalendar.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.R;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class FirebaseManager {
    public static final String REMOTE_CONFIG_RATE_REQUEST_MIN_STARTS = "rate_request_min_starts";
    public static final String REMOTE_CONFIG_AUTO_SCROLL_THRESHOLD = "auto_scroll_top_threshold";
    public static final String REMOTE_CONFIG_AUTO_SCROLL_MINI_OFFSET = "auto_scroll_top_mini_offset";
    public static final String REMOTE_CONFIG_AUTO_SCROLL_NORMAL_OFFSET = "auto_scroll_top_offset";
    public static final String REMOTE_CONFIG_SKIP_INTERSTITIAL_AD_COUNT = "skip_interstitial_ad_count";

    public static final String EVENT_ACTIVITY_ABOUT = "activity_about";
    public static final String EVENT_ACTIVITY_FAVOURITES = "activity_favourites";
    public static final String EVENT_ACTIVITY_NOTIFICATIONS = "activity_notifications";
    public static final String EVENT_ACTIVITY_SETTINGS = "activity_settings";

    public static final String EVENT_ACTION_OPEN_SERIES = "action_open_series";

    public static final String EVENT_ACTION_SET_NOTIFICATION = "action_set_notification";
    public static final String EVENT_ACTION_SET_SERIES_NOTIFICATION = "action_set_series_notification";
    public static final String EVENT_ACTION_SET_NOTIFICATION_REBOOT = "action_set_notification_reboot";
    public static final String EVENT_ACTION_SET_NOTIFICATION_TRIGGERED = "action_triggered_notification";
    public static final String EVENT_ACTION_REMOVE_NOTIFICATION = "action_remove_notification";
    public static final String EVENT_ACTION_UPDATE_NOTIFICATION = "action_update_notification";
    public static final String EVENT_ACTION_OPEN_NOTIFICATION = "action_open_notification";
    public static final String EVENT_ACTION_EXPORT_RACE_TO_CALENDAR = "action_export_to_calendar";

    public static final String EVENT_ACTION_OPEN_RACE_URL = "action_open_race_url";
    public static final String EVENT_ACTION_OPEN_SERIES_URL = "action_open_series_url";
    public static final String EVENT_ACTION_OPEN_SERIES_RESULTS = "action_open_series_results";
    public static final String EVENT_ACTION_OPEN_RACE_RESULTS = "action_open_race_results";

    public static final String EVENT_ACTION_LOAD_PREVIOUS_ALL = "action_load_previous_all";
    public static final String EVENT_ACTION_LOAD_PREVIOUS_FAVOURITES = "action_load_previous_favourites";
    public static final String EVENT_ACTION_LOAD_PREVIOUS_SERIES = "action_load_previous_series";

    public static final String EVENT_ACTION_BACK_SCROLL = "action_back_scroll";

    public static final String ERROR_RESULTS_EMPTY = "error_results_";
    public static final String ERROR_STANDINGS_EMPTY = "error_standings_";


    public static final String EVENT_REQUEST_RATE = "action_request_rate";
    public static final String EVENT_USER_LIKES_APP = "action_user_y_likes_app";
    public static final String EVENT_USER_LIKES_Y_RATES_APP = "action_user_likes_y_rates_app";
    public static final String EVENT_USER_LIKES_N_RATES_APP = "action_user_likes_n_rates_app";

    public static final String EVENT_USER_DISLIKES_APP = "action_user_n_likes_app";
    public static final String EVENT_USER_DISLIKES_Y_SENDS_MAIL = "action_user_dislikes_y_email_app";
    public static final String EVENT_USER_DISLIKES_N_SENDS_MAIL = "action_user_dislikes_n_email_app";


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

    //region Remote Config
    public static void initRemoteConfig(@NonNull final Activity context) {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.fetch().addOnCompleteListener(context, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    remoteConfig.activateFetched();
                }
            }
        });
    }

    public static int getIntRemoteConfig(String what) {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setDefaults(R.xml.config_defaults);
        return (int) remoteConfig.getLong(what);
    }
    //endregion
}
