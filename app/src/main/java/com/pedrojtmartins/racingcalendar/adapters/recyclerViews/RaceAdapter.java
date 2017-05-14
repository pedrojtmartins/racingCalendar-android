package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.RowRace2Binding;
import com.pedrojtmartins.racingcalendar.helpers.APIHelper;
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
                int thisWeekNo = DateFormatter.getThisWeekNumber();
                String dateLbl;
                if (raceWeekNo == thisWeekNo) {
                    // TODO: 14/05/2017 we only need to change the color if there are previous races
//                    int color = APIHelper.getColor(binding.getRoot().getContext().getResources(), R.color.thisWeek);
                    binding.weekTitle.setText(sThisWeek);
//                    binding.weekTitle.setBackgroundColor(color);
                } else if (raceWeekNo == thisWeekNo + 1) {
                    binding.weekTitle.setText(sNextWeek);
//                    binding.weekTitle.setBackgroundColor(0);
                } else {
                    dateLbl = mValues.get(position).getFullDate();
                    dateLbl = DateFormatter.getWeekInterval(dateLbl);
                    binding.weekTitle.setText(dateLbl);
//                    binding.weekTitle.setBackgroundColor(0);
                }

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
                                mCallback.updateAlarm(race, true);
//                                if (mCallback.updateAlarm(race, true)) {
//                                    race.setIsAlarmSet(true);
//                                }
                                break;

                            case R.id.two:
                                if (mCallback.updateAlarm(race, false)) {
                                    race.setIsAlarmSet(false);
                                }
                                break;

                            case R.id.three:
                                mCallback.openUrl(race);
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

        binding.raceRowUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.openUrl(race);
                }
            }
        });

        binding.raceRowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openNotifications(race);
            }
        });
    }

    public static class BindingAdapters {
        @BindingAdapter({"bind:foreground"})
        public static void setFont(FrameLayout layout, int dateState) {
            Drawable drawable = null;
            if (dateState < 0) {
                int color = APIHelper.getColor(layout.getContext().getResources(), R.color.pastEventForeground);
                drawable = new ColorDrawable(color);
            }

            layout.setForeground(drawable);
        }
    }
}
