package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.Models.Series;
import com.pedrojtmartins.racingcalendar.BR;

/**
 * Pedro Martins
 * 11/02/2017
 */

public class SeriesAdapter extends ObservableAdapter<Series> {
    public SeriesAdapter(int itemLayoutId, ObservableArrayList<Series> items) {
        super(itemLayoutId, items);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            Series currSeries = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currSeries);
        }
    }
}
