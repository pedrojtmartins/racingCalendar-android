package com.pedrojtmartins.racingcalendar.views.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Settings;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.SeriesAdapter;
import com.pedrojtmartins.racingcalendar.databinding.FragmentListBinding;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRecyclerViewFragment;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesList;
import com.pedrojtmartins.racingcalendar.models.Series;

public class SeriesListFragment extends Fragment implements IRecyclerViewFragment {

    private static final String STATE_SCROLL_POS = "currSeriesScrollPos";

    private ISeriesList mISeriesList;
    private ISeriesCallback mISeriesCallback;
    private FragmentListBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mBinding.swipeRefresh.setEnabled(false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // By doing it this way we guarantee that databinding will work properly
        // when some change is made to the mList in the viewModel
        ObservableArrayList<Series> list = mISeriesList.getSeriesList();

        // Since we are specifying the item layout here and using databinding
        // we will be able to have different layouts easily without changing the adapter.
        // To achieve that the fragment will need to be aware of the selected layout.
        // We can use shared preferences for that purpose for example.
        //TODO implement multiple layout selection capabilities
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(new SeriesAdapter(R.layout.row_series, list, mISeriesCallback));

//        if (savedInstanceState != null) {
//            int scrollPos = savedInstanceState.getInt(STATE_SCROLL_POS);
//            if (scrollPos != 0)
//                setScrollPos(scrollPos);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mISeriesList = (ISeriesList) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IRaceList");
        }

        try {
            mISeriesCallback = (ISeriesCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ISeriesCallback");
        }
    }

    @Override
    public void itemsReloaded(int count) {
        // Series list won't use this.
    }
    @Override
    public void resetScrollPosToItem0() {
        // Series list won't use this.
    }

    @Override
    public void smoothScrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean isOnTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recyclerView.getLayoutManager();
        if (layoutManager == null)
            return true;

        int firstPos = layoutManager.findFirstVisibleItemPosition();
        return firstPos <= Settings.SCROLL_ON_TOP_NORMAL_OFFSET;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(STATE_SCROLL_POS, scrollPos);
//    }
}
