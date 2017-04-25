package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.INotificationsCallback;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

/**
 * Pedro Martins
 * 22/04/2017
 */

public class NotificationsAdapter extends ObservableAdapter<RCNotification> {
    private final INotificationsCallback mCallback;

    public NotificationsAdapter(int itemLayoutId, ObservableArrayList<RCNotification> items, INotificationsCallback callback) {
        super(itemLayoutId, items);
        mCallback = callback;
    }


    @Override
    public void onBindViewHolder(final ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final RCNotification currNotification = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currNotification);

            setTintForOlderApis(viewHolder.mDataBinding.getRoot(), currNotification.isToDelete(), viewHolder);


            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        boolean res = mCallback.deleteNotification(currNotification);
                        updateStatus(currNotification, res);

                        setTintForOlderApis(view, res, viewHolder);
                    }
                }
            });
        }
    }

    private void setTintForOlderApis(View view, boolean res, ViewHolder viewHolder) {
        if (Build.VERSION.SDK_INT < 21) {
            View v = viewHolder.mDataBinding.getRoot().findViewById(R.id.notification_imv);
            if (v == null)
                return;

            AppCompatImageView imv = (AppCompatImageView) v;
            if (res) {
                imv.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.tintDisabled));
            } else {
                imv.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.tintEnabled));
            }
        }
    }

    private void updateStatus(RCNotification notif, boolean res) {
        notif.setToDelete(res);
    }
}
