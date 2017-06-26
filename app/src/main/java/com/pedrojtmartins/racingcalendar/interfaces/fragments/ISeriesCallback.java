package com.pedrojtmartins.racingcalendar.interfaces.fragments;

import com.pedrojtmartins.racingcalendar.models.Series;

/**
 * Pedro Martins
 * 04/03/2017
 */

public interface ISeriesCallback {
    void displayRacesFromSeries(Series series);

    void openUrl(Series currSeries);
    void openResults(Series currSeries);
}
