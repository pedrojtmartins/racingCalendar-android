package com.pedrojtmartins.racingcalendar.interfaces.fragments;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.models.Series;

/**
 * Pedro Martins
 * 11/02/2017
 */

public interface ISeriesList {
    ObservableArrayList<Series> getSeriesList();
}
