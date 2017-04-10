package com.pedrojtmartins.racingcalendar.interfaces.fragments;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.models.Race;

/**
 * Pedro Martins
 * 29/01/2017
 */

public interface IRaceList {
    ObservableArrayList<Race> getRacesList(boolean favouritesOnly);

    ObservableArrayList<Race> getRacesListBySeries(int seriesId);

    boolean undoFragmentTransition();
}
