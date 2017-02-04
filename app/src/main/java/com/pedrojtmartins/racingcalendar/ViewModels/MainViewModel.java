package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Interfaces.IDataUpdater;
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

    private ObservableArrayList<Race> mRaceList;
    @Override
    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }

    private ObservableArrayList<Series> mSeriesList;
    @Override
    public ObservableArrayList<Series> getSeriesList() {
        return mSeriesList;
    }

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
        if (races != null)
            mRaceList.addAll(races);
    }

    private void initDataUpdate() {
        mApiManager.updateData(this, mSharedPreferencesManager, mDbManager);
    }
}