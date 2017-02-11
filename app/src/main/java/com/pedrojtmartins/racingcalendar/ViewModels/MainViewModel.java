package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Interfaces.ViewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;
import com.pedrojtmartins.racingcalendar.Models.ServerData;
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

    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }
    private ObservableArrayList<Race> mRaceList;

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

        loadDataFromLocalDb();
        initDataUpdate();
    }

    private void loadDataFromLocalDb() {
        mRaceList.clear();
        ArrayList<Race> raceList = mDbManager.getUpcomingRaces();
        if (raceList != null && raceList.size() > 0)
            mRaceList.addAll(raceList);

        mSeriesList.clear();
        ArrayList<Series> seriesList = mDbManager.getSeries();
        if (seriesList != null && seriesList.size() > 0)
            mSeriesList.addAll(seriesList);
    }

    private void initDataUpdate() {
        // TODO: 05/02/2017 - Alert the user the download is starting
        mApiManager.updateDataIfRequired(this, mSharedPreferencesManager.getDataVersion());
    }

    @Override
    public void newDataIsReady(ServerData data) {
        if (data == null)
            return;

        // TODO: 05/02/2017 - Do this in a separate thread?
        // TODO: 05/02/2017 - Alert the user the download is complete and data was updated
        mSharedPreferencesManager.addDataVersion(data.version);
        mDbManager.addRaces(data.races);
        mDbManager.addSeries(data.series);

        loadDataFromLocalDb();
    }
}