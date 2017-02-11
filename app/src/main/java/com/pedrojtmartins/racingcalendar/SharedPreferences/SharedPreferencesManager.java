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
    private final String DATA_VERSION = "dataVersion";
    private final String FAVOURITE_IDS = "favourites";

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

    public String getFavouriteIds() {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getString(FAVOURITE_IDS, "");
    }

    public void addFavouriteIds(String arrFavs) {
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (arrFavs == null || arrFavs.length() == 0) {
            editor.remove(FAVOURITE_IDS);
        } else {
            editor.putString(FAVOURITE_IDS, arrFavs);
        }

        editor.apply();
    }
}
