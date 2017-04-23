package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.models.Series;

/**
 * Pedro Martins
 * 11/02/2017
 */

public class SeriesSelectionAdapter extends ObservableAdapter<Series> {
    public SeriesSelectionAdapter(int itemLayoutId, ObservableArrayList<Series> items) {
        super(itemLayoutId, items);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final Series currSeries = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currSeries);

            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean newState = !currSeries.isFavorite();
                    currSeries.setFavorite(newState);
                }
            });
        }
    }
}
