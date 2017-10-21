package com.pedrojtmartins.racingcalendar.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

    private final String ADS_NOTIFICATIONS = "notificationsOpenCount";
    private final String ADS_URL = "urlOpenCount";

    private final String FORCE_RELEASE_NOTES = "forceReleaseNotes_3";
    private final String FORCE_DB_UPDATE = "forceDBUpdate_3";

    public SharedPreferencesManager(Context context) {
        // TODO: 19/09/2017 this should be a singleton?
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
    public RCSettings getSettings() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String sSettings = sp.getString(SETTINGS_NOTIFICATIONS, "");

        try {
            RCSettings settings = new Gson().fromJson(sSettings, RCSettings.class);
            if (settings == null)
                return new RCSettings(sSettings);

            settings.normalize();
            return settings;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return new RCSettings(sSettings);
        }
    }

    public void addSettings(RCSettings settings) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String serialized = new Gson().toJson(settings);
        editor.putString(SETTINGS_NOTIFICATIONS, serialized);
        editor.apply();
    }
    //endregion

    //region Force data update
    public boolean getForceDataUpdate() {
        return getBoolean(FORCE_DB_UPDATE, true);
    }
    public void setForceDataUpdate() {
        setBoolean(FORCE_DB_UPDATE, false);
    }
    //endregion Force data update

    //region Force release notes update
    public boolean getReleaseNotesUpdate() {
        return getBoolean(FORCE_RELEASE_NOTES, true);
    }
    public void setReleaseNotesUpdate() {
        setBoolean(FORCE_RELEASE_NOTES, false);
    }
    //endregion Force release notes update

    //region Helpers
    private boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    private void setBoolean(String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    //endregion Helpers

    //region RATING
//    public boolean askForRate(String todayDate, int minStarts, int minDays) {
//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//
//        int rated = sp.getInt(RATED, 0);
//        int starts = sp.getInt(SESSION_STARTS, 0);
//        int differentDaysStarts = sp.getInt(DIFFERENT_DAYS_STARTS, 0);
//        String lastDayStart = sp.getString(LAST_DAY_START, "");
//        updateRateData(starts, differentDaysStarts, todayDate);
//
//        if (rated > 0)
//            return false;
//
//        if (starts < minStarts)
//            return false;
//
//        if (differentDaysStarts < minDays)
//            return false;
//
//        if (todayDate.equals(lastDayStart))
//            return false;
//
//
//        return true;
//    }
//
//    private void updateRateData(int starts, int differentDaysStarts, String lastDate, String todayDate) {
//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//
//        editor.putInt(SESSION_STARTS, starts + 1);
//        editor.putInt(DIFFERENT_DAYS_STARTS, starts + 1);
//
//        editor.apply();
//    }
    //endregion
}
