package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.RowRace2Binding;
import com.pedrojtmartins.racingcalendar.databinding.RowRaceDatesBinding;
import com.pedrojtmartins.racingcalendar.eventResults.RaceResultsManager;
import com.pedrojtmartins.racingcalendar.helpers.APIHelper;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.models.Race;

public class RaceAdapter extends ObservableAdapter<Race> {
    private final IRaceList mCallback;
    private final String mThisWeek;
    private final String mNextWeek;
    private final String mRaceShort;

    private final String mSetSingleNotification;
    private final String mRemSingleNotification;
    private final String mSetMultiNotification;
    private final String mRemMultiNotification;

    public RaceAdapter(int itemLayoutId, ObservableArrayList<Race> items, IRaceList iCallback, Resources resources) {
        super(itemLayoutId, items);

        mCallback = iCallback;
        mThisWeek = resources.getString(R.string.thisWeek);
        mNextWeek = resources.getString(R.string.nextWeek);
        mRaceShort = resources.getString(R.string.raceShort);

        mSetSingleNotification = resources.getString(R.string.setSingleAlarm);
        mRemSingleNotification = resources.getString(R.string.removeSingleAlarm);
        mSetMultiNotification = resources.getString(R.string.setMultiAlarm);
        mRemMultiNotification = resources.getString(R.string.removeMultiAlarm);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues == null || mValues.size() <= position) {
            return;
        }

        RowRace2Binding binding = (RowRace2Binding) viewHolder.mDataBinding;
        Race currRace = mValues.get(position);
        binding.setData(currRace);

        // Testing only
//        currRace.setDate("2017-02-04T22:22:22_2017-02-05T23:22:22");

        int thisWeekNo = DateFormatter.getThisWeekNumber();
        int raceWeekNo = DateFormatter.getWeekNumber(currRace.getDate(0));
        // For raceWeekNo we can ignore all the other dates (if they exist)
        // since they will all be in the same week.
        // E.g. Friday_Saturday_Sunday

        showWeekNumberHeaderIfNeeded(position, binding, raceWeekNo, thisWeekNo);
        applyThisWeekVisualsIfNeeded(currRace, raceWeekNo, thisWeekNo);
        hideUpcomingRacesResultsIconIfNeeded(binding, raceWeekNo, thisWeekNo);

        setDatesInfo(binding, currRace);
        listenForAlarmStateChanges(binding, currRace);

        setClickListeners(binding, currRace);
    }


    private void setDatesInfo(RowRace2Binding binding, Race currRace) {
        // Let's set data and change the visibility of the layouts that contain the.
        int datesCount = currRace.getDatesCount();
        setDatesInfo(currRace, datesCount, 2, binding.datesContainer3);
        setDatesInfo(currRace, datesCount, 1, binding.datesContainer2);
        setDatesInfo(currRace, datesCount, 0, binding.datesContainer1);
    }

    private void setDatesInfo(final Race currRace, int datesCount, final int index, RowRaceDatesBinding binding) {
        if (datesCount > index) {
            if (datesCount > 1) {
                String raceIdentifier = mRaceShort + (index + 1);
                binding.raceRowDateIdentifier.setText(raceIdentifier);
                if (binding.raceRowDateIdentifier.getVisibility() != View.VISIBLE) {
                    binding.raceRowDateIdentifier.setVisibility(View.VISIBLE);
                }
            } else {
                if (binding.raceRowDateIdentifier.getVisibility() != View.GONE)
                    binding.raceRowDateIdentifier.setVisibility(View.GONE);
            }

            String hour = currRace.getHour(index);
            if (hour != null && !hour.isEmpty()) {
                binding.raceRowDateHour.setText(hour);
                if (binding.raceRowDateHour.getVisibility() != View.VISIBLE)
                    binding.raceRowDateHour.setVisibility(View.VISIBLE);
            } else {
                if (binding.raceRowDateHour.getVisibility() != View.GONE)
                    binding.raceRowDateHour.setVisibility(View.GONE);
            }

            binding.raceRowDateDate.setText(currRace.getSimplifiedDate(index));
            binding.raceRowDateWeekDay.setText(currRace.getDayOfWeekShort(index));

            if (binding.raceRowDateParent.getVisibility() != View.VISIBLE)
                binding.raceRowDateParent.setVisibility(View.VISIBLE);

            if (currRace.getIsAlarmSet(index)) {
                if (binding.raceRowNotification.getVisibility() != View.VISIBLE) {
                    binding.raceRowNotification.setVisibility(View.VISIBLE);
                }

                binding.raceRowNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.openNotifications(currRace, index);
                    }
                });

            } else {
                if (binding.raceRowNotification.getVisibility() != View.GONE)
                    binding.raceRowNotification.setVisibility(View.GONE);
            }
        } else {
            if (binding.raceRowDateParent.getVisibility() != View.GONE)
                binding.raceRowDateParent.setVisibility(View.GONE);
        }
    }

    private void listenForAlarmStateChanges(final RowRace2Binding binding, final Race currRace) {
        currRace.alarmUpdated.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setDatesInfo(binding, currRace);
            }
        });
    }

    private void showWeekNumberHeaderIfNeeded(int position, RowRace2Binding binding, int raceWeekNo, int thisWeekNo) {
        if (position == 0 || displayTitle(position, raceWeekNo)) {
            String dateLbl;
            if (raceWeekNo == thisWeekNo) {
                binding.weekTitle.setText(mThisWeek);
            } else if (raceWeekNo == thisWeekNo + 1) {
                binding.weekTitle.setText(mNextWeek);
            } else {
                dateLbl = mValues.get(position).getFullDate(0);
                dateLbl = DateFormatter.getWeekInterval(dateLbl);
                binding.weekTitle.setText(dateLbl);
            }

            binding.weekTitle.setVisibility(View.VISIBLE);

        } else {
            binding.weekTitle.setVisibility(View.GONE);
        }
    }

    private void hideUpcomingRacesResultsIconIfNeeded(RowRace2Binding binding, int raceWeekNo, int thisWeekNo) {
        binding.raceRowResultsParent.setVisibility(raceWeekNo > thisWeekNo ? View.GONE : View.VISIBLE);
    }

    private void applyThisWeekVisualsIfNeeded(Race currRace, int raceWeekNo, int thisWeekNo) {
        // This will be responsible for turning the foreground into the "thisWeek" color
        if (raceWeekNo == thisWeekNo && currRace.eventDateStatus > 0) {
            currRace.eventDateStatus = 0;
        }
    }

    private boolean displayTitle(int position, int raceWeekNo) {
        if (position > 0) {
            int lastRaceWeekNo = DateFormatter.getWeekNumber(mValues.get(position - 1).getDate(0));
            if (lastRaceWeekNo != raceWeekNo)
                return true;
        }

        return false;
    }

    private void setClickListeners(final RowRace2Binding binding, final Race race) {
        // This strategy is limiting the number of date notifications to 3.
        // Even if it will be enough for our needs, it is not an elegant solution.
        // TO IMPROVE

        binding.raceRowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(binding.raceRowMore.getContext(), binding.raceRowMore);
                popup.getMenuInflater().inflate(R.menu.row_race, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.set1notif:
                                mCallback.updateAlarm(race, true, 0);
                                break;

                            case R.id.rem1notif:
                                if (mCallback.updateAlarm(race, false, 0)) {
                                    race.setIsAlarmSet(0, false);
                                }
                                break;

                            case R.id.set2notif:
                                mCallback.updateAlarm(race, true, 1);
                                break;
//
                            case R.id.rem2notif:
                                if (mCallback.updateAlarm(race, false, 1)) {
                                    race.setIsAlarmSet(1, false);
                                }
                                break;
//
                            case R.id.set3notif:
                                mCallback.updateAlarm(race, true, 2);
                                break;
//
                            case R.id.rem3notif:
                                if (mCallback.updateAlarm(race, false, 2)) {
                                    race.setIsAlarmSet(2, false);
                                }
                                break;

                            case R.id.three:
                                mCallback.openUrl(race);
                                break;

                            case R.id.openResults:
                                mCallback.openResults(race);
                                break;
                        }
                        return true;
                    }
                });

                int datesCount = race.getDatesCount();
                Menu menu = popup.getMenu();
                showCorrectMenu(datesCount, menu.findItem(R.id.set1notif), menu.findItem(R.id.rem1notif), race, 0);
                showCorrectMenu(datesCount, menu.findItem(R.id.set2notif), menu.findItem(R.id.rem2notif), race, 1);
                showCorrectMenu(datesCount, menu.findItem(R.id.set3notif), menu.findItem(R.id.rem3notif), race, 2);
                showCorrectMenu(datesCount, menu.findItem(R.id.set3notif), menu.findItem(R.id.rem3notif), race, 2);

                boolean resultsAvailable = RaceResultsManager.areResultsAvailable(race.getSeriesId());
                menu.findItem(R.id.openResults).setVisible(resultsAvailable);

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

        binding.raceRowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.openResults(race);
                }
            }
        });
    }
    private void showCorrectMenu(final int datesCount, final MenuItem itemSet, final MenuItem itemRem, final Race race, final int index) {
        if (datesCount <= index) {
            itemSet.setVisible(false);
            itemRem.setVisible(false);
        } else {
            if (!race.getIsAlarmSet(index)) {
                itemSet.setVisible(true);
                itemRem.setVisible(false);

                if (datesCount > 1) {
                    itemSet.setTitle(String.format(mSetMultiNotification, index + 1));
                } else {
                    itemSet.setTitle(mSetSingleNotification);
                }

            } else {
                itemSet.setVisible(false);
                itemRem.setVisible(true);

                if (datesCount > 1) {
                    itemRem.setTitle(String.format(mRemMultiNotification, index + 1));
                } else {
                    itemRem.setTitle(mRemSingleNotification);
                }
            }
        }
    }

    public static class BindingAdapters {
        @BindingAdapter({"bind:background"})
        public static void setFont(LinearLayout layout, int dateState) {
            int color = 0;
            if (dateState == 0) {
                color = APIHelper.getColor(layout.getContext().getResources(), R.color.raceThisWeekBackground);
            } else if (dateState < 0) {
                color = APIHelper.getColor(layout.getContext().getResources(), R.color.racePastBackground);
            } else {
                color = APIHelper.getColor(layout.getContext().getResources(), R.color.raceNormalBackground);
            }

            layout.setBackgroundColor(color);
        }
    }
}