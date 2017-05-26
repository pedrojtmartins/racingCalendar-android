package com.pedrojtmartins.racingcalendar.interfaces.fragments;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;

/**
 * Pedro Martins
 * 29/01/2017
 */

public interface IRaceList {
    ObservableArrayList<Race> getRacesList(boolean favouritesOnly);

    ObservableArrayList<Race> getRacesListBySeries(int seriesId);

    boolean undoFragmentTransition();

    boolean updateAlarm(Race race, boolean state, int index);
//    boolean updateAlarm(Series series, boolean state);

    void openUrl(Race race);

    void openNotifications(Race race, int index);


    void loadPrevious(boolean mFavouritesOnly);
    void loadPrevious(Series mSeries);
}
