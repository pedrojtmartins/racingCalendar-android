package com.pedrojtmartins.racingcalendar.interfaces.fragments;

import com.pedrojtmartins.racingcalendar.models.RCNotification;

/**
 * Pedro Martins
 * 22/04/2017
 */

public interface INotificationsCallback {
    boolean updateNotification(RCNotification notification, boolean delete);
}
