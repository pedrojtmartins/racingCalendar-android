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
    private final String NOTIFICATIONS_SET = "notifsSet";
    private final String SETTINGS_NOTIFICATIONS = "notifSettings";
    private final String RELEASE_NOTES1 = "notes1";

    public SharedPreferencesManager(Context context) {
        mContext = context;
    }

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

    public int getNotificationsSetCount() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(NOTIFICATIONS_SET, 0);
    }

    public void addNotificationsSet() {
        int count = getNotificationsSetCount() + 1;

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(NOTIFICATIONS_SET, count);
        editor.apply();
    }

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
