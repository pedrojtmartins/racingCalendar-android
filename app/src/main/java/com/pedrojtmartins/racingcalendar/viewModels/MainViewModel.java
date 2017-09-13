package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.interfaces.viewModels.IDataUpdater;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
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
    public String updatedFromServerNewSeries;

    public ObservableBoolean newAppUpdate;
    public ObservableBoolean firstInitialization;

    public ObservableArrayList<Race> getRacesList(boolean favouritesOnly) {
        if (favouritesOnly)
            return mFavouriteRaces;
        else
            return mAllRaces;
    }

    public ObservableArrayList<Race> getRacesList(int seriesId) {
        ArrayList<Race> aList = mDbManager.getRaces(seriesId);
        if (aList == null)
            return null;

        mSeriesRaces.clear();
        mSeriesRaces.addAll(aList);
        return mSeriesRaces;
    }

    private ObservableArrayList<Race> mSeriesRaces;
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
        mSeriesRaces = new ObservableArrayList<>();

        updatedFromServer = new ObservableInt(0);
        newAppUpdate = new ObservableBoolean(false);
        firstInitialization = new ObservableBoolean(false);
    }

    public void initialize() {
        loadDataFromLocalDb();
    }

    public boolean displayReleaseNotes() {
        return mSharedPreferencesManager.getReleaseNotesUpdate();
    }

    public void releaseNotesDismissed() {
        mSharedPreferencesManager.setReleaseNotesUpdate();
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
        ArrayList<Race> favsList = mDbManager.getRaces(true);
        if (favsList != null && favsList.size() > 0)
            mFavouriteRaces.addAll(favsList);

        mAllRaces.clear();
        ArrayList<Race> raceList = mDbManager.getRaces(false);
        if (raceList != null && raceList.size() > 0)
            mAllRaces.addAll(raceList);

        if (mAllRaces == null || mAllRaces.isEmpty() || mSeriesList == null || mSeriesList.isEmpty()) {
            mSharedPreferencesManager.addDataVersion(-1);
        }

        if (mSeriesList.isEmpty() && mAllRaces.isEmpty()) {
            firstInitialization.set(true);
        }
    }

    private void initDataUpdate() {
        // TODO: 05/02/2017 - Alert the user the download is starting
        int dataVersion = mSharedPreferencesManager.getDataVersion();
        boolean forceUpdate = mSharedPreferencesManager.getForceDataUpdate();
        mApiManager.updateDataIfRequired(this, dataVersion, forceUpdate);
    }

    @Override
    public void newDataIsReady(ServerData data) {
        if (data == null)
            return;

        // TODO: 05/02/2017 - Do this in a separate thread?
        mSharedPreferencesManager.addDataVersion(data.version);
        mDbManager.addRaces(data.races);
        mDbManager.addSeries(data.series);

        // Find the names of the series added if any
        // so we can inform the user
        updatedFromServerNewSeries = findNewSeriesNamesAdded(data);
        updatedFromServer.set(updatedFromServer.get() + 1);

        loadDataFromLocalDb();

        boolean forceUpdate = mSharedPreferencesManager.getForceDataUpdate();
        if (forceUpdate)
            mSharedPreferencesManager.setForceDataUpdate();
    }

    private String findNewSeriesNamesAdded(ServerData data) {
        // Will only search in case we already have some series. i.e. it is a new update
        if (mSeriesList == null || mSeriesList.isEmpty() || data == null || data.newSeries == null || data.newSeries.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        for (int id : data.newSeries) {
            Series series = Series.getSeries(data.series, id);
            if (series != null) {
                sb.append("\n-").append(series.getName());
            }
        }

        return sb.toString();
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

    public int loadPrevious(boolean favouritesOnly) {
        ArrayList<Race> previousRaces = mDbManager.getRaces(favouritesOnly, false);
        if (previousRaces != null && previousRaces.size() > 0) {
            if (favouritesOnly) {
                mFavouriteRaces.addAll(0, previousRaces);
            } else {
                mAllRaces.addAll(0, previousRaces);
            }

            return previousRaces.size();
        }
        return 0;
    }

    public int loadPrevious(Series series) {
        if (series == null)
            return 0;

        ArrayList<Race> previousRaces = mDbManager.getRaces(series.getId(), false);
        if (previousRaces != null && previousRaces.size() > 0) {
            mSeriesRaces.addAll(0, previousRaces);
            return previousRaces.size();
        }

        return 0;
    }

    public RCNotification addNotification(final Race race, final int minutesBefore, final int index) {
        RCNotification rcNotification = mDbManager.getNotificationForEvent(race.getId(), index);
        if (rcNotification != null)
            return rcNotification;

        // TODO: 26/05/2017  
        rcNotification = new RCNotification(race.getId(), race.getSeriesId(), race.getFullDate(index), index, minutesBefore);
        long id = mDbManager.addNotification(rcNotification);
        if (id <= 0) {// TODO: 20/04/2017 Log error
            return null;
        }

        propagateRaceAlarmUpdate(race, true, index);
        return mDbManager.getNotification(id);
    }

    public boolean removeNotification(final Race race, final int index) {
        RCNotification rcNotification = mDbManager.getNotificationForEvent(race.getId(), index);
        if (rcNotification != null && mDbManager.removeNotification(rcNotification) == 1) {
            propagateRaceAlarmUpdate(race, false, index);
            return true;
        }
        return false;
    }

    private void propagateRaceAlarmUpdate(final Race race, final boolean newState, final int index) {
        int iAll = mAllRaces.indexOf(race);
        if (iAll != -1) {
            mAllRaces.get(iAll).setIsAlarmSet(index, newState);
        }
        int iFav = mFavouriteRaces.indexOf(race);
        if (iFav != -1) {
            mFavouriteRaces.get(iFav).setIsAlarmSet(index, newState);
        }
    }

    public String getFullUrl(Race race) {
        if (race == null || mSeriesList == null || mSeriesList.isEmpty())
            return null;

        Series series = null;
        for (int i = 0; i < mSeriesList.size(); i++) {
            Series currSeries = mSeriesList.get(i);
            if (currSeries.getId() == race.getSeriesId()) {
                series = currSeries;
                break;
            }
        }

        if (series == null)
            return null;

        return series.getFullUrl() + race.getUrl();
    }

    public boolean openLinksInBrowser() {
        RCSettings settings = mSharedPreferencesManager.getSettings();
        return settings != null && settings.openLinksInBrowser;
    }

}