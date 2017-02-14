package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Models.Series;

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

    public void saveFavourites() {
        mDatabaseManager.setSeriesFavorite(mSeries);
    }
}
