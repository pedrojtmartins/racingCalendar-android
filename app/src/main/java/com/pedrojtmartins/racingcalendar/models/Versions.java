package com.pedrojtmartins.racingcalendar.models;

import com.google.gson.annotations.SerializedName;

/**
 * Pedro Martins
 * 01/02/2017
 */

public class Versions {

    @SerializedName("r")
    private int racesVersion;
    public int getRacesVersion() {
        return racesVersion;
    }
    public void setRacesVersion(int racesVersion) {
        this.racesVersion = racesVersion;
    }

    @SerializedName("s")
    private int seriesVersion;
    public int getSeriesVersion() {
        return seriesVersion;
    }
    public void setSeriesVersion(int seriesVersion) {
        this.seriesVersion = seriesVersion;
    }
}
