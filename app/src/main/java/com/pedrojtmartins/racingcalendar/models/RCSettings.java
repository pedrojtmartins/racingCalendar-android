package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.pedrojtmartins.racingcalendar._settings.Settings;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class RCSettings extends BaseObservable {
    @Bindable
    public boolean notificationsRemember;

    @Bindable
    private String notifMinutesBefore;
    public String getNotificationMinutesBefore() {
        return notifMinutesBefore;
    }
    public void setNotificationMinutesBefore(String notificationMinutesBefore) {
        if (notificationMinutesBefore.isEmpty())
            notificationMinutesBefore = "0";

        this.notifMinutesBefore = notificationMinutesBefore;
    }

    public boolean openLinksInBrowser;

    public RCSettings(String serialized) {
        setDefaults();

        if (serialized != null && !serialized.isEmpty()) {
            String[] aSettings = serialized.split(";");
            if (aSettings.length > 0)
                notificationsRemember = aSettings[0].equals("1");

            if (aSettings.length > 1)
                notifMinutesBefore = aSettings[1];

            if (aSettings.length > 2)
                openLinksInBrowser = aSettings[2].equals("1");
        }
    }

    private void setDefaults() {
        notificationsRemember = Settings.NOTIFICATION_REMEMBER;
        notifMinutesBefore = Settings.NOTIFICATION_MINUTES_BEFORE;
        openLinksInBrowser = Settings.OPEN_LINK_IN_BROWSER;
    }

    @Override
    public String toString() {
        return (notificationsRemember ? "1" : "0") +
                ";" + notifMinutesBefore +
                ";" + (openLinksInBrowser ? "1" : "0");
    }
}
