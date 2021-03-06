package com.pedrojtmartins.racingcalendar.viewModels;

import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class SettingsViewModel {
    private final SharedPreferencesManager mSharedPreferencesManager;
    public RCSettings mSettings;

    public SettingsViewModel(SharedPreferencesManager sharedPreferencesManager) {
        mSharedPreferencesManager = sharedPreferencesManager;
        loadSettings();
    }

    private void loadSettings() {
        mSettings = mSharedPreferencesManager.getSettings();
    }

    public void saveChanges() {
        mSharedPreferencesManager.addSettings(mSettings);
    }

    public boolean needsLayoutUpdate() {
        return mSettings.miniLayoutChanged();
    }
    public boolean needsSeriesUpdate() {
        return mSettings.showPreviousYearsChanged();
    }

    public void setMiniLayoutActive(boolean state) {
        mSettings.setMiniLayoutAllActive(state);
        mSettings.setMiniLayoutFavActive(state);
        mSettings.setMiniLayoutSeriesActive(state);
    }
}
