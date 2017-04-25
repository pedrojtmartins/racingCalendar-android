package com.pedrojtmartins.racingcalendar.viewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.models.Series;

/**
 * Pedro Martins
 * 13/02/2017
 */

public class FavoritesViewModel {
    private final DatabaseManager mDatabaseManager;

    private ObservableArrayList<Series> mSeries;

    public ObservableArrayList<Series> getSeries() {
        return mSeries;
    }

    public FavoritesViewModel(DatabaseManager databaseManager) {
        mDatabaseManager = databaseManager;

        mSeries = new ObservableArrayList<>();

        loadAllSeriesFromLocalDb();
    }

    private void loadAllSeriesFromLocalDb() {
        mSeries.clear();
        mSeries.addAll(mDatabaseManager.getSeries());
    }

    public void saveFavorites() {
        mDatabaseManager.setSeriesFavorite(mSeries);
    }

    public boolean somethingToChange() {
        return true;
    }
}
