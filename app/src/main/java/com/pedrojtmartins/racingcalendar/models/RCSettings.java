package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Pedro Martins
 * 24/04/2017
 */

public class RCSettings extends BaseObservable {
    @Bindable
    public boolean notifDefaults;

    @Bindable
    public String notifMinutesBefore;

    public RCSettings(String serialized) {
        setDefaults();

        if (serialized != null && !serialized.isEmpty()) {
            String[] aSettings = serialized.split(";");
            if (aSettings.length > 0)
                notifDefaults = aSettings[0].equals("1");

            if (aSettings.length > 1)
                notifMinutesBefore = aSettings[1];
        }
    }

    private void setDefaults() {
        notifDefaults = false;
        notifMinutesBefore = "0";
    }

    @Override
    public String toString() {
        return (notifDefaults ? "1" : "0") + ";" + notifMinutesBefore;
    }
}
