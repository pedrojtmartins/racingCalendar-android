package com.pedrojtmartins.racingcalendar.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Pedro Martins
 * 28/01/2017
 */

//@Parcel(Parcel.Serialization.BEAN)
public class Race extends BaseObservable {
    private int mId;
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    private int mSeriesId;
    public int getSeriesId() {
        return mSeriesId;
    }
    public void setSeriesId(int seriesId) {
        mSeriesId = seriesId;
    }

    @Bindable
    private String mName;
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    @Bindable
    private String mTrack;
    public String getTrack() {
        return mTrack;
    }
    public void setTrack(String track) {
        mTrack = track;
    }

    @Bindable
    private String mLocation;
    public String getLocation() {
        return mLocation;
    }
    public void setLocation(String location) {
        mLocation = location;
    }

    @Bindable
    private String mDate;
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

    public Race(int id, int seriesId, String name, String track, String location, String date) {
        mId = id;
        mSeriesId = seriesId;
        mName = name;
        mTrack = track;
        mLocation = location;
        mDate = date;
    }
}
