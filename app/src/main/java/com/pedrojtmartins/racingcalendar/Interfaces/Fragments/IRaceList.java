package com.pedrojtmartins.racingcalendar.Interfaces.Fragments;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Models.Race;

/**
 * Pedro Martins
 * 29/01/2017
 */

public interface IRaceList {
    ObservableArrayList<Race> getRacesList(boolean favouritesOnly);
}
