package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;

/**
 * Pedro Martins
 * 17/04/2017
 */

public class RCNotification extends BaseObservable {
    public int id;
    public int raceId;
    public int seriesId;
    public String time;
    public int minutesBefore;
    public String seriesName;

    @Bindable
    public String getDateTime() {
        String date = DateFormatter.getSimplifiedDate(time);
        String hour = DateFormatter.getHour(time);
        return date + "  " + hour;
    }

    @Bindable
    private boolean mToDelete;

    public boolean isToDelete() {
        return mToDelete;
    }

    public void setToDelete(boolean mToDelete) {
        this.mToDelete = mToDelete;
        notifyPropertyChanged(BR.toDelete);
    }

    public RCNotification(int id, int raceId, int seriesId, String time, int minutesBefore, String seriesName) {
        this.id = id;
        this.raceId = raceId;
        this.seriesId = seriesId;
        this.time = time;
        this.minutesBefore = minutesBefore;
        this.seriesName = seriesName;
    }

    public RCNotification(int eventId, int seriesId, String time, int minutesBefore) {
        this.raceId = eventId;
        this.seriesId = seriesId;
        this.time = time;
        this.minutesBefore = minutesBefore;
        this.seriesName = "";
    }
}
