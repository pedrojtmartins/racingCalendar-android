package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.Helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.databinding.RowRaceBinding;

public class RaceAdapter extends ObservableAdapter<Race> {
    public RaceAdapter(int itemLayoutId, ObservableArrayList<Race> items) {
        super(itemLayoutId, items);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            Race currRace = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currRace);

            boolean displayTitle = false;
            if (position > 0) {
                int lastWeekNo = DateFormatter.getWeekNumber(mValues.get(position - 1).getDate());
                int thisWeekNo = DateFormatter.getWeekNumber(currRace.getDate());
                if (lastWeekNo != thisWeekNo)
                    displayTitle = true;
            }

            RowRaceBinding binding = (RowRaceBinding) viewHolder.mDataBinding;
            if (position == 0 || displayTitle) {
                String fullDate = mValues.get(position).getFullDate();
                String formattedDate = DateFormatter.getWeekInterval(fullDate);
                binding.weekTitle.setText(formattedDate);
                binding.weekTitle.setVisibility(View.VISIBLE);
            } else {
                binding.weekTitle.setVisibility(View.GONE);
            }
        }
    }
}
