package com.pedrojtmartins.racingcalendar.Adapters.RecyclerViews;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.Models.Series;

/**
 * Pedro Martins
 * 11/02/2017
 */

public class SeriesAdapter extends ObservableAdapter<Series> {
    private final ISeriesCallback mCallback;

    public SeriesAdapter(int itemLayoutId, ObservableArrayList<Series> items, ISeriesCallback callback) {
        super(itemLayoutId, items);

        mCallback = callback;
    }


    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final Series currSeries = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currSeries);
            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null)
                        mCallback.displayRacesFromSeries(currSeries.getId());
                }
            });
        }
    }
}
