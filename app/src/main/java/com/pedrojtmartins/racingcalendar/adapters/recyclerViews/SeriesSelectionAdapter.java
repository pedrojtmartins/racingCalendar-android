package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
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
    public void onBindViewHolder(final ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final Series currSeries = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currSeries);

            setTintForOlderApis(viewHolder.mDataBinding.getRoot(), currSeries.isFavorite(), viewHolder);

            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !currSeries.isFavorite();
                    currSeries.setFavorite(newState);

                    setTintForOlderApis(view, newState, viewHolder);
                }
            });
        }
    }

    private void setTintForOlderApis(View view, boolean newState, ViewHolder viewHolder) {
        if (Build.VERSION.SDK_INT < 21) {
            View v = viewHolder.mDataBinding.getRoot().findViewById(R.id.favourites_imv);
            if (v == null)
                return;

            AppCompatImageView imv = (AppCompatImageView) v;
            if (newState) {
                imv.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.tintEnabled));
            } else {
                imv.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.tintDisabled));
            }
        }
    }
}
