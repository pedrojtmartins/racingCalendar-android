package com.pedrojtmartins.racingcalendar.Interfaces.ViewModels;

import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 04/02/2017
 */

public interface IDataUpdater {
    ArrayList<Race> getRaceList();
    ArrayList<Series> getSeriesList();
}
