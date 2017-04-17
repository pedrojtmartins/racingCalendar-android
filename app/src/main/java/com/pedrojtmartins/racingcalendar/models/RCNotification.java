package com.pedrojtmartins.racingcalendar.models;

/**
 * Created by pedro on 17/04/2017.
 */

public class RCNotification {
    public int id;
    public int eventId;
    public boolean isSeries;
    public int minutesBefore;

    public RCNotification(int id, int eventId, boolean isSeries, int minutesBefore) {
        this.id = id;
        this.eventId = eventId;
        this.isSeries = isSeries;
        this.minutesBefore = minutesBefore;
    }

    public RCNotification(int eventId, boolean isSeries, int minutesBefore) {
        this.eventId = eventId;
        this.isSeries = isSeries;
        this.minutesBefore = minutesBefore;
    }
}
