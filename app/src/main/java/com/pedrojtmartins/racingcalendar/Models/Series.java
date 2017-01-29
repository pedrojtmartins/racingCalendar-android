package com.pedrojtmartins.racingcalendar.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class Series extends BaseObservable {
    private int mId;
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
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
    private int mYear;
    public int getYear() {
        return mYear;
    }
    public void setYear(int year) {
        mYear = year;
    }

    public Series(int id, String name, int year) {
        mId = id;
        mName = name;
        mYear = year;
    }
}
