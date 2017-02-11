package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
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
