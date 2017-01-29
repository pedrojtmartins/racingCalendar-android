package com.pedrojtmartins.racingcalendar.ViewModels;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.Models.Race;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class MainViewModel implements IRaceList {
    private ObservableArrayList<Race> mRaceList;

    @Override
    public ObservableArrayList<Race> getList() {
        return null;
    }
}
