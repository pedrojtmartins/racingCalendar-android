package com.pedrojtmartins.racingcalendar.models;

/**
 * Pedro Martins
 * 25/09/2017
 */

public class InternalCalendars {
    public int id;
    public String displayName;
    public String accountName;
    public String ownerName;


    public InternalCalendars(int calID, String displayName, String accountName, String ownerName) {
        this.id = calID;
        this.displayName = displayName;
        this.accountName = accountName;
        this.ownerName = ownerName;
    }
}

