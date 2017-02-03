package com.pedrojtmartins.racingcalendar.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.pedrojtmartins.racingcalendar.Helpers.DateFormatter;

/**
 * Pedro Martins
 * 28/01/2017
 */

//@Parcel(Parcel.Serialization.BEAN)
public class Race extends BaseObservable {
    @SerializedName("i")
    private int mId;
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    @SerializedName("s")
    private int mSeriesId;
    public int getSeriesId() {
        return mSeriesId;
    }
    public void setSeriesId(int seriesId) {
        mSeriesId = seriesId;
    }

    @SerializedName("u")
    private int mRaceNumber;
    public int getRaceNumber() {
        return mRaceNumber;
    }
    public String getRaceNumberString() {
        return Integer.toString(mRaceNumber);
    }
    public void setRaceNumber(int raceNumber) {
        mRaceNumber = raceNumber;
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

    @SerializedName("l")
    @Bindable
    private String mLocation;
    public String getLocation() {
        return mLocation;
    }
    public void setLocation(String location) {
        mLocation = location;
    }

    @SerializedName("d")
    @Bindable
    private String mDate;
    public String getFullDate() {
        return mDate;
    }
    public String getSimplifiedDate() {
        return DateFormatter.getSimplifiedDate(mDate);
    }
    public String getDate() {
        if (mDate != null && mDate.contains("T"))
            return mDate.split("T")[0];

        return "";
    }
    public String getHour() {
        if (mDate != null && mDate.contains("T"))
            return mDate.split("T")[1];

        return "";
    }
    public void setDate(String date) {
        mDate = date;
    }

    public Race(int id, int seriesId, int raceNumber, String name, String location, String date) {
        mId = id;
        mSeriesId = seriesId;
        mRaceNumber = raceNumber;
        mName = name;
        mLocation = location;
        mDate = date;
    }
}
