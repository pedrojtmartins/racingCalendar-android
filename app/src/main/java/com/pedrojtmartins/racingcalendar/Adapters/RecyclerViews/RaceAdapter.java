package com.pedrojtmartins.racingcalendar.Adapters.RecyclerViews;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.Helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.RowRaceBinding;

public class RaceAdapter extends ObservableAdapter<Race> {
    private final String sThisWeek;
    private final String sNextWeek;

    public RaceAdapter(int itemLayoutId, ObservableArrayList<Race> items, Resources resources) {
        super(itemLayoutId, items);

        sThisWeek = resources.getString(R.string.thisWeek);
        sNextWeek = resources.getString(R.string.nextWeek);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            Race currRace = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currRace);

            int raceWeekNo = DateFormatter.getWeekNumber(currRace.getDate());

            boolean displayTitle = false;
            if (position > 0) {
                int lastRaceWeekNo = DateFormatter.getWeekNumber(mValues.get(position - 1).getDate());
                if (lastRaceWeekNo != raceWeekNo)
                    displayTitle = true;
            }

            RowRaceBinding binding = (RowRaceBinding) viewHolder.mDataBinding;
            if (position == 0 || displayTitle) {

                String dateLbl;
                int thisWeekNo = DateFormatter.getThisWeekNumber();
                if (raceWeekNo == thisWeekNo) {
                    dateLbl = sThisWeek;
                } else if (raceWeekNo == thisWeekNo + 1) {
                    dateLbl = sNextWeek;
                } else {
                    dateLbl = mValues.get(position).getFullDate();
                    dateLbl = DateFormatter.getWeekInterval(dateLbl);
                }

                binding.weekTitle.setText(dateLbl);
                binding.weekTitle.setVisibility(View.VISIBLE);
            } else {
                binding.weekTitle.setVisibility(View.GONE);
            }
        }
    }
}
