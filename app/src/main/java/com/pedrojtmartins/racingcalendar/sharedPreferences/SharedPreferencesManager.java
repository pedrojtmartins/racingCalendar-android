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
    private final String FORCE_DATA_UPDATE = "forceDataUpdate_v7";

    private final String ADS_NOTIFICATIONS = "notificationsOpenCount";
    private final String ADS_URL = "urlOpenCount";

//    private final String SESSION_STARTS = "session_starts";
//    private final String DIFFERENT_DAYS_STARTS = "days_starts";
//    private final String LAST_DAY_START = "last_day_start";
//    private final String RATED = "rated"; //0-no, 1-yes, 2-do not ask

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
    public RCSettings getSettings() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return new RCSettings(sp.getString(SETTINGS_NOTIFICATIONS, ""));
    }

    public void addSettings(String settings) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SETTINGS_NOTIFICATIONS, settings);
        editor.apply();
    }
    //endregion

    //region Force data update
    public boolean getForceDataUpdate() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getBoolean(FORCE_DATA_UPDATE, true);
    }

    public void setForceDataUpdate() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(FORCE_DATA_UPDATE, false);
        editor.apply();
    }
    //region Force data update

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

//    //Not used yet
//    public boolean getReleaseNotes1Shown() {
//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        return sp.getBoolean(RELEASE_NOTES1, false);
//    }
//
//    public void setReleaseNotes1Seen() {
//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(RELEASE_NOTES1, true);
//        editor.apply();
//    }
}
