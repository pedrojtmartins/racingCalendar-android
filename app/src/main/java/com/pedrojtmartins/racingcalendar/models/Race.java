package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;

import com.google.gson.annotations.SerializedName;
import com.pedrojtmartins.racingcalendar._settings.Settings;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;

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
        return "#" + Integer.toString(mRaceNumber);
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
    private String mDates;
    // Dates var can have multiples dates. In case they do, they will be divided by a special character.
    // This strategy was chosen for performance purposes. This way, there is no need to process all
    // races when loading from the database, just when they are presented to the user, hence the extra
    // processing implemented on the getters.
    // E.g. Single date:   2017-02-23T08:00:00
    //      Multiple date: 2017-02-23T08:00:00_2017-02-24T09:00:00

    public int getDatesCount() {
        if (mDates == null)
            return 0;

        return mDates.split(Settings.RACE_DATES_DIVIDER).length;
    }

    public String getUnformattedDate() {
        return mDates;
    }

    public String getFullDate(int index) {
        if (mDates == null)
            return "";

        String[] dates = mDates.split(Settings.RACE_DATES_DIVIDER);
        if (index >= dates.length)
            return "";

        return dates[index];
    }

    public String getSimplifiedDate(int index) {
        String date = getFullDate(index);
        return DateFormatter.getSimplifiedDate(date);
    }

    public String getDayOfWeekShort(int index) {
        String date = getFullDate(index);
        return DateFormatter.getDayOfWeekShort(date);
    }

    public String getDate(int index) {
        String date = getFullDate(index);
        return DateFormatter.getDate(date);
    }

    public String getHour(int index) {
        String date = getFullDate(index);
        return DateFormatter.getHour(date);
    }

    public void setDate(String date) {
        mDates = date;
    }

    public boolean hasDateOnly(int index) {
        String date = getFullDate(index);
        return date == null || !date.contains("T");
    }

    @SerializedName("w")
    private String mUrl;

    public String getUrl() {
        if (mUrl == null)
            return "";

        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public boolean hasUrl() {
        return mUrl != null && !mUrl.isEmpty();
    }

    @Bindable
    private String mSeriesName;

    public String getSeriesName() {
        return mSeriesName;
    }

    public void setSeriesName(String seriesName) {
        mSeriesName = seriesName;
    }

    @Bindable
    private ObservableArrayList<Boolean> mIsAlarmSet;

    public boolean getIsAlarmSet(int index) {
        if (mIsAlarmSet == null || mIsAlarmSet.size() <= index)
            return false;

        return mIsAlarmSet.get(index);
    }

    public void setIsAlarmSet(int index, boolean alarmSet) {
        if (mIsAlarmSet == null || mIsAlarmSet.size() <= index)
            return;

        mIsAlarmSet.set(index, alarmSet);
        alarmUpdated.set(alarmUpdated.get() + 1);
    }

    public ObservableInt alarmUpdated;

    @Bindable
    public int eventDateStatus;

    public Race(int id,
                int seriesId,
                int raceNumber,
                String name,
                String location,
                String date,
                String seriesName,
                String url,
                boolean upcoming) {
        mId = id;
        mSeriesId = seriesId;
        mRaceNumber = raceNumber;
        mName = name;
        mLocation = location;
        mDates = date;
        mSeriesName = seriesName;
        mUrl = url;
        eventDateStatus = upcoming ? 1 : -1;

        int datesCount = getDatesCount();
        if (datesCount > 0) {
            mIsAlarmSet = new ObservableArrayList<>();
            for (int i = 0; i < datesCount; i++)
                mIsAlarmSet.add(false);
        }

        alarmUpdated = new ObservableInt(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Race race = (Race) o;

        return mId == race.mId;

    }

    @Override
    public int hashCode() {
        return mId;
    }


}
