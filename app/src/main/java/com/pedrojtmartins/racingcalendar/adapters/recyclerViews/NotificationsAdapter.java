package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.view.View;

import com.pedrojtmartins.racingcalendar.BR;
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
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues != null && mValues.size() > position) {
            final RCNotification currNotification = mValues.get(position);
            viewHolder.mDataBinding.setVariable(BR.data, currNotification);

            viewHolder.mDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallback != null) {
                        boolean res = mCallback.deleteNotification(currNotification);
                        updateStatus(currNotification, res);
                    }
                }
            });
        }
    }

    private void updateStatus(RCNotification notif, boolean res) {
        notif.setToDelete( res);
    }


}
