package com.pedrojtmartins.racingcalendar.Adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.Helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.databinding.RowRaceBinding;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final int mItemLayoutId;
    private final ObservableArrayList<Race> mValues;

    public RecyclerViewAdapter(int itemLayoutId, ObservableArrayList<Race> items) {
        mItemLayoutId = itemLayoutId;
        mValues = items;

        if (mValues != null)
            mValues.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList>() {
                @Override
                public void onChanged(ObservableList ts) {
                    notifyDataSetChanged();
                }
                @Override
                public void onItemRangeChanged(ObservableList ts, int i, int i1) {
                    notifyDataSetChanged();
                }
                @Override
                public void onItemRangeInserted(ObservableList ts, int i, int i1) {
                    notifyDataSetChanged();
                }
                @Override
                public void onItemRangeMoved(ObservableList ts, int i, int i1, int i2) {
                    notifyDataSetChanged();
                }
                @Override
                public void onItemRangeRemoved(ObservableList ts, int i, int i1) {
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowRaceBinding mBinding = DataBindingUtil.inflate(inflater, mItemLayoutId, parent, false);
        return new ViewHolder(mBinding);
    }
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if (mValues != null && mValues.size() > position) {
            Race currRace = mValues.get(position);
            holder.mDataBinding.setVariable(BR.data, currRace);

            boolean displayTitle = false;
            if (position > 0) {
                int lastWeekNo = DateFormatter.getWeekNumber(mValues.get(position - 1).getDate());
                int thisWeekNo = DateFormatter.getWeekNumber(currRace.getDate());
                if (lastWeekNo != thisWeekNo)
                    displayTitle = true;
            }

            if (position == 0 || displayTitle) {
                String fullDate = mValues.get(position).getFullDate();
                String formattedDate = DateFormatter.getWeekInterval(fullDate);
                holder.mDataBinding.weekTitle.setText(formattedDate);
                holder.mDataBinding.weekTitle.setVisibility(View.VISIBLE);
            } else {
                holder.mDataBinding.weekTitle.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null)
            return 0;

        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RowRaceBinding mDataBinding;
        private ViewHolder(RowRaceBinding dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }
    }
}
