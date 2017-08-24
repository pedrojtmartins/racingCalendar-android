package com.pedrojtmartins.racingcalendar.adapters.recyclerViews;

import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.models.EventResultUnit;

/**
 * Pedro Martins
 * 26/06/2017
 */

public class ResultsAdapter extends ObservableAdapter<EventResultUnit> {

//    int cFirst = -1;
//    int cSecond = -1;
//    int cThird = -1;

    public ResultsAdapter(int itemLayoutId, ObservableArrayList<EventResultUnit> items) {
        super(itemLayoutId, items);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder viewHolder, int position) {
        if (mValues == null || mValues.size() <= position) {
            return;
        }

        ViewDataBinding binding = viewHolder.mDataBinding;
        EventResultUnit currResult = mValues.get(position);
        binding.setVariable(BR.result, currResult);

//        setBackgroundColors(binding, currResult);
    }

//    private void setBackgroundColors(RowResultsBinding binding, EventResultUnit currResult) {
//        switch (currResult.position) {
//            case "1":
//                if (cFirst == -1)
//                    cFirst = binding.getRoot().getContext().getResources().getColor(R.color.firstPosition);
//
//                binding.getRoot().setBackgroundColor(cFirst);
//                binding.
//                break;
//
//            case "2":
//                if (cSecond == -1)
//                    cSecond = binding.getRoot().getContext().getResources().getColor(R.color.secondPosition);
//
//                binding.getRoot().setBackgroundColor(cSecond);
//                break;
//
//            case "3":
//                if (cThird == -1)
//                    cThird = binding.getRoot().getContext().getResources().getColor(R.color.thirdPosition);
//
//                binding.getRoot().setBackgroundColor(cThird);
//                break;
//
//            default:
//                binding.getRoot().setBackgroundColor(0);
//        }
//    }
}
