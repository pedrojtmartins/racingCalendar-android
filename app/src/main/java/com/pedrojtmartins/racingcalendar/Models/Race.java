package com.pedrojtmartins.racingcalendar.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

/**
 * Pedro Martins
 * 28/01/2017
 */

//@Parcel(Parcel.Serialization.BEAN)
public class Race extends BaseObservable {
    @SerializedName("id")
    private int mId;
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    @SerializedName("sid")
    private int mSeriesId;
    public int getSeriesId() {
        return mSeriesId;
    }
    public void setSeriesId(int seriesId) {
        mSeriesId = seriesId;
    }

    @SerializedName("nm")
    @Bindable
    private String mName;
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }

    @SerializedName("trk")
    @Bindable
    private String mTrack;
    public String getTrack() {
        return mTrack;
    }
    public void setTrack(String track) {
        mTrack = track;
    }

    @SerializedName("loc")
    @Bindable
    private String mLocation;
    public String getLocation() {
        return mLocation;
    }
    public void setLocation(String location) {
        mLocation = location;
    }

    @SerializedName("dt")
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
