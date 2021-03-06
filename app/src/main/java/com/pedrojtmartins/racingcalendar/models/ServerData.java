package com.pedrojtmartins.racingcalendar.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 04/02/2017
 */

public class ServerData {
    @SerializedName("r")
    public ArrayList<Race> races;

    @SerializedName("s")
    public ArrayList<Series> series;

    @SerializedName("n")
    public ArrayList<Integer> newSeries;

    @SerializedName("v")
    public int version;
}
