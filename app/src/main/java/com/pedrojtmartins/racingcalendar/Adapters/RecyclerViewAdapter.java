package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.BR;

import java.util.ArrayList;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int mItemLayoutId;
    private final ArrayList<T> mValues;

    public RecyclerViewAdapter(int itemLayoutId, ArrayList<T> items) {
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
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if (mValues != null && mValues.size() > position)
            holder.getDataBinding().setVariable(BR.data, mValues.get(position));
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;

        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mDataBinding;

        public ViewDataBinding getDataBinding() {
            return mDataBinding;
        }

        ViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }
    }
}
