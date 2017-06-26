package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;

import com.pedrojtmartins.racingcalendar.databinding.RowResultsBinding;
import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class ResultsAdapter extends ObservableAdapter<EventResultUnit> {

    public ResultsAdapter(int itemLayoutId, ObservableArrayList<EventResultUnit> items) {
        super(itemLayoutId, items);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues == null || mValues.size() <= position) {
            return;
        }

        RowResultsBinding binding = (RowResultsBinding) viewHolder.mDataBinding;
        EventResultUnit currResult = mValues.get(position);
        binding.setResult(currResult);
    }
}
