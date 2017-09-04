package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.view.View;
import android.widget.LinearLayout;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.RowSeriesBinding;
import com.pedrojtmartins.racingcalendar.helpers.APIHelper;
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

                binding.seriesResults.setOnClickListener(new View.OnClickListener() {
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

    public static class BindingAdapters {
        @BindingAdapter({"app:seriesBackground"})
        public static void setBackground(LinearLayout layout, int dateState) {
            int color;
            if (dateState >= 100) {
                color = APIHelper.getColor(layout.getContext().getResources(), R.color.racePastBackground);
            } else {
                color = APIHelper.getColor(layout.getContext().getResources(), R.color.raceNormalBackground);
            }

            layout.setBackgroundColor(color);
        }
    }
}
