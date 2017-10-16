package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.eventResults.StandingsResultsManager;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class Series extends BaseObservable {
    //region Id
    @SerializedName("i")
    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
    //endregion

    //region Name
    @SerializedName("n")
    @Bindable
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
    //endregion

    //region Year
    @SerializedName("y")
    @Bindable
    private int mYear;

    public int getYear() {
        return mYear;
    }

    @Bindable
    public String getSYear() {
        return mYear + "";
    }

    public void setYear(int year) {
        mYear = year;
    }
    //endregion

    //region URL
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
    public String getFullUrl() {
        String url = "";

        if (mUrl != null) {
            url += mUrl;
        }

        if (mUrlPrefix != null) {
            url += mUrlPrefix;
        }

        return url;
    }
    public boolean hasUrl() {
        return mUrl != null && !mUrl.isEmpty();
    }
    public boolean hasResultsUrl() {
        return StandingsResultsManager.areResultsAvailable(mId);
    }
    //endregion

    @SerializedName("s")
    private int nextSeriesId;
    public int getNextSeriesId() {
        return nextSeriesId;
    }
    public void setNextSeriesId(int nextSeriesId) {
        this.nextSeriesId = nextSeriesId;
    }

    @SerializedName("p")
    private int previousSeriesId;
    public int getPreviousSeriesId() {
        return previousSeriesId;
    }
    public void setPreviousSeriesId(int previousSeriesId) {
        this.previousSeriesId = previousSeriesId;
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

    public Series(int id, String name, int year, boolean isFavorite, int totalRaces, int currRace, String url, String urlPrefix, int prevYId, int nextYId) {
        mId = id;
        mName = name;
        mYear = year;
        mFavorite = isFavorite;
        mTotalRaces = totalRaces;
        mCurrentRace = currRace;
        mUrl = url;
        mUrlPrefix = urlPrefix;
        previousSeriesId = prevYId;
        nextSeriesId = nextYId;
    }

    public static Series getSeries(ArrayList<Series> list, int id) {
        if (list == null || list.isEmpty() || id < 0)
            return null;

        for (Series currSeries : list) {
            if (currSeries.mId == id)
                return currSeries;
        }

        return null;
    }
}
