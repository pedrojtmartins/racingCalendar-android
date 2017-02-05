package com.pedrojtmartins.racingcalendar.Interfaces.ViewModels;

import com.pedrojtmartins.racingcalendar.Models.ServerData;

/**
 * Pedro Martins
 * 04/02/2017
 */

public interface IDataUpdater {
    void newDataIsReady(ServerData data);
}
