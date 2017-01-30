package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.API.APIManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Models.Race;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel {
    private final DatabaseManager mDbManager;

    private ObservableArrayList<Race> mRaceList;
    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }

    public MainViewModel(DatabaseManager dbManager) {
        mDbManager = dbManager;
        mRaceList = new ObservableArrayList<>();

        loadRaceListFromLocalDb();
//        initListsUpdate();
    }

    private void loadRaceListFromLocalDb() {
        ArrayList<Race> races = mDbManager.getRaces();
        if (races != null)
            mRaceList.addAll(races);

        //TODO delete when db is populated
        mRaceList.add(new Race(1, 1, "name1", "track1", "location1", "date1"));
        mRaceList.add(new Race(2, 1, "name2", "track2", "location2", "date2"));
        mRaceList.add(new Race(3, 1, "name3", "track3", "location3", "date3"));
    }

    private void initListsUpdate() {
        APIManager.getApi().getVersions().enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {

            }
            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {

            }
        });
    }

    private void downloadSeriesListFromAPI() {

    }
    private void downloadRacesListFromAPI() {
        APIManager.getApi().getAllRaces().enqueue(new Callback<List<Race>>() {
            @Override
            public void onResponse(Call<List<Race>> call, Response<List<Race>> response) {

            }
            @Override
            public void onFailure(Call<List<Race>> call, Throwable t) {

            }
        });
    }


}