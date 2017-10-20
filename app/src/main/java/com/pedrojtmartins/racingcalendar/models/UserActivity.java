package com.pedrojtmartins.racingcalendar.models;

/**
 * pedro.martins
 * 20/10/2017.
 */

public class UserActivity {

    public final int id;
    public final String date;
    public final String time;
    public final boolean isRequest;
    public final boolean isAccepted;

    public UserActivity(String date, String time, boolean isRequest, boolean isAccepted) {
        this.id = -1;
        this.date = date;
        this.time = time;
        this.isRequest = isRequest;
        this.isAccepted = false;
    }

    public UserActivity(int id, String date, String time, boolean isRequest, boolean isAccepted) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.isRequest = isRequest;
        this.isAccepted = isAccepted;
    }
}
