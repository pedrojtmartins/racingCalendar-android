package com.pedrojtmartins.racingcalendar.models;

/**
 * Created by pedro on 17/04/2017.
 */

public class RCNotification {
    public int id;
    public int raceId;
    public int seriesId;
    public String time;
    public int minutesBefore;
    public String seriesName;

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
