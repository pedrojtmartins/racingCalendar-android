package com.pedrojtmartins.racingcalendar.models;

/**
 * Created by pedro on 17/04/2017.
 */

public class RCNotification {
    public int id;
    public int eventId;
    public String time;
    public int minutesBefore;

    public RCNotification(int id, int eventId, String time, int minutesBefore) {
        this.id = id;
        this.eventId = eventId;
        this.time = time;
        this.minutesBefore = minutesBefore;
    }

    public RCNotification(int eventId, String time, int minutesBefore) {
        this.eventId = eventId;
        this.time = time;
        this.minutesBefore = minutesBefore;
    }
}
