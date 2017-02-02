package com.pedrojtmartins.racingcalendar.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Pedro Martins
 * 01/02/2017
 */

public class SharedPreferencesManager {
    private Context mContext;

    private final String SHARED_PREFS = "sharedPrefs";
    private final String RACE_VERSION = "raceVersion";
    private final String SERIES_VERSION = "seriesVersion";

    public SharedPreferencesManager(Context context) {
        mContext = context;
    }

    public int getRaceVersion() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(RACE_VERSION, 0);
    }
    public void addRaceVersion(int version) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(RACE_VERSION, version);
        editor.apply();
    }

    public int getSeriesVersion() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(SERIES_VERSION, 0);
    }
    public void addSeriesVersion(int version) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SERIES_VERSION, version);
        editor.apply();
    }
}
