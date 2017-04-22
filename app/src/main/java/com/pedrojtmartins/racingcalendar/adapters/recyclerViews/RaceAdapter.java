package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.RowRace2Binding;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.models.Race;

public class RaceAdapter extends ObservableAdapter<Race> {
    private final String sThisWeek;
    private final String sNextWeek;
    private final IRaceList mCallback;

    public RaceAdapter(int itemLayoutId, ObservableArrayList<Race> items, IRaceList iCallback, Resources resources) {
        super(itemLayoutId, items);

        mCallback = iCallback;
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

            RowRace2Binding binding = (RowRace2Binding) viewHolder.mDataBinding;
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

            setClickListeners(binding, currRace);
        }
    }

    private void setClickListeners(final RowRace2Binding binding, final Race race) {
        binding.raceRowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(binding.raceRowMore.getContext(), binding.raceRowMore);
                popup.getMenuInflater().inflate(R.menu.row_race, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                if (mCallback.updateAlarm(race, true)) {
                                    race.setIsAlarmSet(true);
                                }
                                break;

                            case R.id.two:
                                if (mCallback.updateAlarm(race, false)) {
                                    race.setIsAlarmSet(false);
                                }
                                break;


                        }
                        return true;
                    }
                });

                Menu menu = popup.getMenu();
                menu.findItem(R.id.one).setVisible(!race.getIsAlarmSet());
                menu.findItem(R.id.two).setVisible(race.getIsAlarmSet());

                popup.show();
            }
        });
    }
}
