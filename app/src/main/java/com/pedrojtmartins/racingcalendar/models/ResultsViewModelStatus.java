package com.pedrojtmartins.racingcalendar.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.pedrojtmartins.racingcalendar.BR;

/**
 * Pedro Martins
 * 01/08/2017
 */

public class ResultsViewModelStatus extends BaseObservable {
    private boolean isLoadComplete;
    @Bindable
    public boolean isLoadComplete() {
        return isLoadComplete;
    }
    public void setLoadComplete(boolean loadComplete) {
        isLoadComplete = loadComplete;
        notifyPropertyChanged(BR.loadComplete);
    }
}