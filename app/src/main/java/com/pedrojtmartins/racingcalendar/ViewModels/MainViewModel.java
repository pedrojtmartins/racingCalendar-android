package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel {
    private final DatabaseManager mDbManager;
    private final ApiManager mApiManager;

    private ObservableArrayList<Race> mRaceList;
    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }

    private ObservableArrayList<Series> mSeriesList;
    public ObservableArrayList<Series> getSeriesList() {
        return mSeriesList;
    }

    public MainViewModel(DatabaseManager dbManager, ApiManager apiManager ) {
        mDbManager = dbManager;
        mApiManager = apiManager;

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
        mApiManager.updateRaceDataAsync(mRaceList);
        mApiManager.updateSeriesDataAsync(mSeriesList);
    }
}