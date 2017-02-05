package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Interfaces.ViewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;
import com.pedrojtmartins.racingcalendar.SharedPreferences.SharedPreferencesManager;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel implements IDataUpdater {
    private final DatabaseManager mDbManager;
    private final ApiManager mApiManager;
    private final SharedPreferencesManager mSharedPreferencesManager;

    @Override
    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }
    private ObservableArrayList<Race> mRaceList;

    @Override
    public ObservableArrayList<Series> getSeriesList() {
        return mSeriesList;
    }
    private ObservableArrayList<Series> mSeriesList;

    public MainViewModel(DatabaseManager dbManager, ApiManager apiManager, SharedPreferencesManager sharedPreferencesManager) {
        mDbManager = dbManager;
        mApiManager = apiManager;
        mSharedPreferencesManager = sharedPreferencesManager;

        mRaceList = new ObservableArrayList<>();
        mSeriesList = new ObservableArrayList<>();

        loadRaceListFromLocalDb();
        initDataUpdate();
    }

    private void loadRaceListFromLocalDb() {
        ArrayList<Race> races = mDbManager.getRaces();
        if (races != null && races.size() > 0)
            mRaceList.addAll(races);
    }

    private void initDataUpdate() {
        mApiManager.updateData(this, mSharedPreferencesManager, mDbManager);
    }
}