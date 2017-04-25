package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.interfaces.viewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.models.ServerData;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel implements IDataUpdater {
    private final DatabaseManager mDbManager;
    private final APIManager mApiManager;
    private final SharedPreferencesManager mSharedPreferencesManager;

    //This will be observed by the activity and will display a message when needed
    public ObservableInt updatedFromServer;
    public ObservableBoolean newAppUpdate;

    public ObservableArrayList<Race> getRacesList(boolean favouritesOnly) {
        if (favouritesOnly)
            return mFavouriteRaces;
        else
            return mAllRaces;
    }

    public ObservableArrayList<Race> getRacesList(int seriesId) {
        ArrayList<Race> aList = mDbManager.getUpcomingRaces(seriesId);
        if (aList == null)
            return null;

        ObservableArrayList<Race> list = new ObservableArrayList<>();
        list.addAll(aList);
        return list;
    }

    private ObservableArrayList<Race> mFavouriteRaces;
    private ObservableArrayList<Race> mAllRaces;

    public ObservableArrayList<Series> getSeriesList() {
        return mSeriesList;
    }

    private ObservableArrayList<Series> mSeriesList;

    public MainViewModel(DatabaseManager dbManager, APIManager apiManager, SharedPreferencesManager sharedPreferencesManager) {
        mDbManager = dbManager;
        mApiManager = apiManager;
        mSharedPreferencesManager = sharedPreferencesManager;

        mFavouriteRaces = new ObservableArrayList<>();
        mAllRaces = new ObservableArrayList<>();
        mSeriesList = new ObservableArrayList<>();

        updatedFromServer = new ObservableInt(0);
        newAppUpdate = new ObservableBoolean(false);

        loadDataFromLocalDb();

        // recheckUpdates will be called from the onStart method on the activity instead
        //initDataUpdate();
        //checkAppVersion();
    }

    public void recheckUpdates() {
        checkAppVersion();
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

        if (mAllRaces == null || mAllRaces.isEmpty() || mSeriesList == null || mSeriesList.isEmpty()) {
            mSharedPreferencesManager.addDataVersion(-1);
        }
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

    private void checkAppVersion() {
        int currVersion = AppVersionHelper.getCurrentAppVersionCode();
        mApiManager.checkAppVersion(this, currVersion);
    }

    @Override
    public void newAppVersionIsAvailable() {
        newAppUpdate.set(true);
    }

    public void reload() {
        loadDataFromLocalDb();
    }

    public RCNotification addNotification(final Race race, final int minutesBefore) {
        RCNotification rcNotification = mDbManager.getNotificationForEvent(race.getId());
        if (rcNotification != null)
            return rcNotification;

        rcNotification = new RCNotification(race.getId(), race.getSeriesId(), race.getFullDate(), minutesBefore);
        long id = mDbManager.addNotification(rcNotification);
        if (id <= 0) {// TODO: 20/04/2017 Log error
            return null;
        }

        propagateRaceAlarmUpdate(race, true);
        return mDbManager.getNotification(id);
    }

    public boolean removeNotification(final Race race) {
        RCNotification rcNotification = mDbManager.getNotificationForEvent(race.getId());
        if (rcNotification != null && mDbManager.removeNotification(rcNotification) == 1) {
            propagateRaceAlarmUpdate(race, false);
            return true;
        }
        return false;
    }

    private void propagateRaceAlarmUpdate(Race race, boolean newState) {
        // TODO: 23/04/2017 bad solution. probably there should be only 1 list?
        int iAll = mAllRaces.indexOf(race);
        if (iAll != -1) {
            mAllRaces.get(iAll).setIsAlarmSet(newState);
        }
        int iFav = mFavouriteRaces.indexOf(race);
        if (iFav != -1) {
            mFavouriteRaces.get(iFav).setIsAlarmSet(newState);
        }
    }
}