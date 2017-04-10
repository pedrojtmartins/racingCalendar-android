package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Pedro Martins
 * 11/02/2017
 */

abstract class ObservableAdapter<T> extends RecyclerView.Adapter<ObservableAdapter.ViewHolder> {

    private final int mItemLayoutId;
    final ObservableArrayList<T> mValues;


    ObservableAdapter(int itemLayoutId, ObservableArrayList<T> items) {
        mItemLayoutId = itemLayoutId;
        mValues = items;

        mValues.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                notifyDataSetChanged();
            }
            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }
            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }
            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                notifyDataSetChanged();
            }
            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding mBinding = DataBindingUtil.inflate(inflater, mItemLayoutId, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;

        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding mDataBinding;
        private ViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }
    }
}
