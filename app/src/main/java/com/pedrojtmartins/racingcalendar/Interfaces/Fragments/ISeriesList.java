package com.pedrojtmartins.racingcalendar.Interfaces.Fragments;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Models.Series;

/**
 * Pedro Martins
 * 11/02/2017
 */

public interface ISeriesList {
    ObservableArrayList<Series> getSeriesList();
}
