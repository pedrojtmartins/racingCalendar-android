package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.databinding.RowSeriesBinding;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.models.Series;

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

            if (viewHolder.mDataBinding instanceof RowSeriesBinding) {
                RowSeriesBinding binding = (RowSeriesBinding) viewHolder.mDataBinding;
                binding.seriesUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.openUrl(currSeries);
                    }
                });

                binding.results.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.openResults(currSeries);
                    }
                });
            }

            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null)
                        mCallback.displayRacesFromSeries(currSeries);
                }
            });
        }
    }
}
