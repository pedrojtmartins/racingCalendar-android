package com.pedrojtmartins.racingcalendar.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("f")
    @Bindable
    private boolean mIsFavorite;
    public boolean isFavorite() {
        return mIsFavorite;
    }
    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    @Bindable
    private int mCurrentRace;
    public int getCurrentRace() {
        return mCurrentRace;
    }
    public void setCurrentRace(int currentRace) {
        mCurrentRace = currentRace;
    }

    @Bindable
    private int mTotalRaces;
    public int getTotalRaces() {
        return mTotalRaces;
    }
    public void setTotalRaces(int totalRaces) {
        mTotalRaces = totalRaces;
    }

    public Series(int id, String name, int year, boolean isFavorite, int totalRaces, int currRace) {
        mId = id;
        mName = name;
        mYear = year;
        mIsFavorite = isFavorite;
        mTotalRaces = totalRaces;
        mCurrentRace = currRace;
    }
}
