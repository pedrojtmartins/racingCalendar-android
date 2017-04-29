package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.pedrojtmartins.racingcalendar.BR;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class Series extends BaseObservable {
    @SerializedName("i")
    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @SerializedName("n")
    @Bindable
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @SerializedName("y")
    @Bindable
    private int mYear;

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    @SerializedName("w")
    private String mUrl;

    public String getUrl() {
        return mUrl;
    }

    public void setmUrl(String url) {
        this.mUrl = url;
    }

    @SerializedName("x")
    private String mUrlPrefix;

    public String getUrlPrefix() {
        return mUrlPrefix;
    }

    public void setUrlPrefix(String mUrlPrefix) {
        this.mUrlPrefix = mUrlPrefix;
    }

    @Bindable
    private boolean mFavorite;

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
        notifyPropertyChanged(BR.favorite);
    }

    @Bindable
    private int mCurrentRace;

    public int getCurrentRace() {
        return mCurrentRace;
    }

    public int getNextRace() {
        return mCurrentRace + 1;
    }

    public String getNextRaceString() {
        return "#" + getNextRace();
    }

    public void setCurrentRace(int currentRace) {
        mCurrentRace = currentRace;
    }

    @Bindable
    private int mTotalRaces;

    public int getTotalRaces() {
        return mTotalRaces;
    }

    public String getTotalRacesString() {
        return getTotalRaces() + "";
    }

    public void setTotalRaces(int totalRaces) {
        mTotalRaces = totalRaces;
    }

    @Bindable
    private int percentageCompleted = -1;

    public int getPercentageCompleted() {
        if (percentageCompleted == -1) {
            percentageCompleted = 0;
            if (getCurrentRace() > 0) {
                percentageCompleted = getCurrentRace() * 100 / getTotalRaces();
            }
        }
        return percentageCompleted;
    }

    public Series(int id, String name, int year, boolean isFavorite, int totalRaces, int currRace, String url, String urlPrefix) {
        mId = id;
        mName = name;
        mYear = year;
        mFavorite = isFavorite;
        mTotalRaces = totalRaces;
        mCurrentRace = currRace;
        mUrl = url;
        mUrlPrefix = urlPrefix;
    }
}
