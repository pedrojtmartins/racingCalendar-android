package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;

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

    //This will be observed by the activity and will display a message when needed
    public ObservableInt updatedFromServer;

    public ObservableArrayList<Race> getRacesList(boolean favouritesOnly) {
        if (favouritesOnly)
            return mFavouriteRaces;
        else
            return mAllRaces;
    }
    private ObservableArrayList<Race> mFavouriteRaces;
    private ObservableArrayList<Race> mAllRaces;

    public ObservableArrayList<Series> getSeriesList() {
        return mSeriesList;
    }
    private ObservableArrayList<Series> mSeriesList;

    public MainViewModel(DatabaseManager dbManager, ApiManager apiManager, SharedPreferencesManager sharedPreferencesManager) {
        mDbManager = dbManager;
        mApiManager = apiManager;
        mSharedPreferencesManager = sharedPreferencesManager;

        mFavouriteRaces = new ObservableArrayList<>();
        mAllRaces = new ObservableArrayList<>();
        mSeriesList = new ObservableArrayList<>();

        updatedFromServer = new ObservableInt(0);

        loadDataFromLocalDb();
        initDataUpdate();
    }

    private void loadDataFromLocalDb() {
        mSeriesList.clear();
        ArrayList<Series> seriesList = mDbManager.getSeries();
        if (seriesList != null && seriesList.size() > 0)
            mSeriesList.addAll(seriesList);

        mFavouriteRaces.clear();
        ArrayList<Race> favsList = mDbManager.getUpcomingRaces(true);
        if (favsList != null && favsList.size() > 0)
            mFavouriteRaces.addAll(favsList);

        mAllRaces.clear();
        ArrayList<Race> raceList = mDbManager.getUpcomingRaces(false);
        if (raceList != null && raceList.size() > 0)
            mAllRaces.addAll(raceList);
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

        updatedFromServer.set(updatedFromServer.get() + 1);
    }

    public void updateFavorites() {
        loadDataFromLocalDb();
    }
}