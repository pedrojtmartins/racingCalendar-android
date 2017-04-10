package com.pedrojtmartins.racingcalendar.interfaces.viewModels;

import com.pedrojtmartins.racingcalendar.models.ServerData;

/**
 * Pedro Martins
 * 04/02/2017
 */

public interface IDataUpdater {
    void newDataIsReady(ServerData data);

    void newAppVersionIsAvailable();
}
