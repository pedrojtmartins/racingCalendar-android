package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.databinding.RowNotificationBinding;
import com.pedrojtmartins.racingcalendar.helpers.APIHelper;
import com.pedrojtmartins.racingcalendar.helpers.ParsingHelper;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.INotificationsCallback;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class NotificationsAdapter extends ObservableAdapter<RCNotification> {
    private final INotificationsCallback mCallback;

    private final long mFocusRaceId;
    private int mDateIndex;

    public NotificationsAdapter(int itemLayoutId, ObservableArrayList<RCNotification> items, INotificationsCallback callback) {
        super(itemLayoutId, items);
        mCallback = callback;
        mFocusRaceId = 0;
    }

    public NotificationsAdapter(int itemLayoutId, ObservableArrayList<RCNotification> items, INotificationsCallback callback, long focusId, int dateIndex) {
        super(itemLayoutId, items);
        mCallback = callback;
        mFocusRaceId = focusId;
        mDateIndex = dateIndex;
    }


    @Override
    public void onBindViewHolder(final ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final RCNotification currNotification = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currNotification);

            setTintForOlderApis(currNotification.isToDelete(), viewHolder);

            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        boolean res = mCallback.updateNotification(currNotification, true);
                        updateStatus(currNotification, res);

                        setTintForOlderApis(res, viewHolder);
                    }
                }
            });

            if (viewHolder.mDataBinding instanceof RowNotificationBinding) {
                RowNotificationBinding binding = (RowNotificationBinding) viewHolder.mDataBinding;
                binding.notificationEditTimeBefore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = viewHolder.mDataBinding.getRoot().getContext();
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        AlertDialogHelper.displayNewNotificationDialog(
                                context,
                                layoutInflater,
                                currNotification.minutesBefore + "",
                                false,
                                new Handler(new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        String timeBefore = msg.getData().getString("timeBefore");
                                        if (timeBefore == null || timeBefore.isEmpty())
                                            return true;

                                        currNotification.setTimeBefore(ParsingHelper.stringToInt(timeBefore));
                                        boolean res = mCallback.updateNotification(currNotification, false);

                                        return true;
                                    }
                                }));
                    }
                });
            }

            if (currNotification.raceId == mFocusRaceId && currNotification.timeIndex == mDateIndex) {
                Resources resources = viewHolder.mDataBinding.getRoot().getContext().getResources();
                if (resources != null) {
                    int color = APIHelper.getColor(resources, R.color.notificationFocus);
                    if (color != 0 && viewHolder.mDataBinding instanceof RowNotificationBinding) {
                        ((RowNotificationBinding) viewHolder.mDataBinding).notificationParent.setBackgroundColor(color);
                    }
                }
            }
        }
    }

    private void setTintForOlderApis(boolean res, ViewHolder viewHolder) {
        if (Build.VERSION.SDK_INT < 21) {
            View v1 = viewHolder.mDataBinding.getRoot().findViewById(R.id.notification_imv);
            if (v1 != null) {
                AppCompatImageView imv = (AppCompatImageView) v1;
                setColorFilter(imv, res);
            }

            View v2 = viewHolder.mDataBinding.getRoot().findViewById(R.id.notification_edit_time_before);
            if (v2 != null) {
                AppCompatImageView imv = (AppCompatImageView) v2;
                setColorFilter(imv, res);
            }
        }
    }

    private void setColorFilter(AppCompatImageView imv, boolean res) {
        if (imv != null) {
            if (res) {
                imv.setColorFilter(ContextCompat.getColor(imv.getContext(), R.color.tintDisabled));
            } else {
                imv.setColorFilter(ContextCompat.getColor(imv.getContext(), R.color.tintEnabled));
            }
        }
    }

    private void updateStatus(RCNotification notif, boolean res) {
        notif.setToDelete(res);
    }

}
