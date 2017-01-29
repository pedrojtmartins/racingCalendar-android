package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.BR;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int mItemLayoutId;
    private final ObservableArrayList<T> mValues;

    public RecyclerViewAdapter(int itemLayoutId, ObservableArrayList<T> items) {
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
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if (mValues != null && mValues.size() > position)
            holder.mDataBinding.setVariable(BR.data, mValues.get(position));
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;

        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mDataBinding;
        private ViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }
    }
}
