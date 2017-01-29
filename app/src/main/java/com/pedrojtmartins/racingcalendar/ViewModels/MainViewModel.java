package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Models.Race;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel {
    private ObservableArrayList<Race> mRaceList;
    public ObservableArrayList<Race> getRaceList() {
        return mRaceList;
    }

    public MainViewModel() {
        mRaceList = new ObservableArrayList<>();
        mRaceList.add(new Race(1, 1, "name1", "track1", "location1", "date1"));
        mRaceList.add(new Race(2, 1, "name2", "track2", "location2", "date2"));
        mRaceList.add(new Race(3, 1, "name3", "track3", "location3", "date3"));
    }
}
