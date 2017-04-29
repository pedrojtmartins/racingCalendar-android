package com.pedrojtmartins.racingcalendar.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.pedrojtmartins.racingcalendar.models.RCSettings;

/**
 * Pedro Martins
 * 01/02/2017
 */

public class SharedPreferencesManager {
    private Context mContext;

    private final String SHARED_PREFS = "sharedPrefs";
    private final String DATA_VERSION = "dataVersion";
    private final String SETTINGS_NOTIFICATIONS = "notifSettings";
    private final String RELEASE_NOTES1 = "notes1";

    private final String ADS_NOTIFICATIONS = "notifsSet";
    private final String ADS_URL = "notifsSet";

    public SharedPreferencesManager(Context context) {
        mContext = context;
    }

    //region Data
    public int getDataVersion() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(DATA_VERSION, 0);
    }

    public void addDataVersion(int version) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(DATA_VERSION, version);
        editor.apply();
    }
    //endregion

    //region Ads
    //region Notifications
    public int getNotificationAdsShownCount() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(ADS_NOTIFICATIONS, 0);
    }

    public void notificationAdShown() {
        int count = getNotificationAdsShownCount() + 1;

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ADS_NOTIFICATIONS, count);
        editor.apply();
    }

    //endregion

    //region URL
    public int getUrlAdsShownCount() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(ADS_URL, 0);
    }

    public void urlAdShown() {
        int count = getUrlAdsShownCount() + 1;

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ADS_URL, count);
        editor.apply();
    }
    //endregion
    //endregion

    //region Settings
    public RCSettings getNotificationsSettings() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return new RCSettings(sp.getString(SETTINGS_NOTIFICATIONS, ""));
    }

    public void addNotificationsSettings(String settings) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SETTINGS_NOTIFICATIONS, settings);
        editor.apply();
    }
    //endregion

    //Not used yet
    public boolean getReleaseNotes1Shown() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(RELEASE_NOTES1, false);
    }

    public void setReleaseNotes1Seen() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(RELEASE_NOTES1, true);
        editor.apply();
    }
}
