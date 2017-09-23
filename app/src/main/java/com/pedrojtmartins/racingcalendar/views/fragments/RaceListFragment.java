package com.pedrojtmartins.racingcalendar.views.fragments;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Settings;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.RaceAdapter;
import com.pedrojtmartins.racingcalendar.databinding.FragmentListBinding;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRecyclerViewFragment;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;


public class RaceListFragment extends Fragment implements IRecyclerViewFragment {
    private IRaceList mIRaceList;
    private FragmentListBinding mBinding;
    private ObservableArrayList<Race> mList;

    private boolean mFavouritesOnly;
    private Series mSeries;

    private int scrollPos = 0;

    public Fragment newInstance(final boolean favouritesOnly) {
        RaceListFragment f = new RaceListFragment();
        f.mFavouritesOnly = favouritesOnly;

        return f;
    }

    public Fragment newInstance(final Series series) {
        RaceListFragment f = new RaceListFragment();
        f.mSeries = series;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        // This is needed to keep track of the scroll position. When the user presses back
        // the app will scroll up if is not on the top.
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollPos += dy;
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSeries != null) {
                    mIRaceList.loadPrevious(mSeries);
                } else {
                    mIRaceList.loadPrevious(mFavouritesOnly);
                }

                // After we load the previous rows
                // disable the swiping functionality
                mBinding.swipeRefresh.setEnabled(false);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // By doing it this way we guarantee that databinding will work properly
        // when some change is made to the mList in the viewModel
        if (mSeries != null) {
            setupHeader();
            mList = mIRaceList.getRacesListBySeries(mSeries.getId());
        } else {
            mList = mIRaceList.getRacesList(mFavouritesOnly);
            mBinding.listHeader.setVisibility(View.GONE);
        }

        // Since we are specifying the item layout here and using databinding
        // we will be able to have different layouts easily without changing the adapter.
        // To achieve that the fragment will need to be aware of the selected layout.
        // We can use shared preferences for that purpose for example.
        //TODO implement multiple layout selection capabilities
//        mBinding.recyclerView.setLayoutManager(new SmoothScrollerLinearLayoutManager(getActivity()));

        RCSettings settings = new SharedPreferencesManager(getContext()).getSettings();
        boolean isMiniLayoutActive = settings.isMiniLayoutAllActive;

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(new RaceAdapter(
                R.layout.row_race2,
                mList,
                mIRaceList,
                getResources(),
                isMiniLayoutActive));

        // When transitioning between fragments the recyclerview could be
        // initialized not on the top because of fragment recycling.
        // This forces it to the top every time.
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mBinding.recyclerView.smoothScrollBy(0, -Integer.MAX_VALUE);
                scrollPos = 0;
            }
        });
    }

    private void setupHeader() {
        mBinding.setVariable(BR.data, mSeries);
        mBinding.listHeader.setVisibility(View.VISIBLE);
        mBinding.listHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) view.getContext()).onBackPressed();
//                mIRaceList.undoFragmentTransition();
            }
        });

        mBinding.seriesHeaderResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIRaceList.openResults(mSeries);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mIRaceList = (IRaceList) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IRaceList");
        }
    }

    @Override
    public void smoothScrollToTop() {
        mBinding.recyclerView.smoothScrollBy(0, -scrollPos);
    }

    @Override
    public boolean isOnTop() {
        return Math.abs(scrollPos) < Settings.SCROLL_ON_TOP_THRESHOLD;
    }

    @Override
    public void itemsReloaded(int count) {
        mBinding.recyclerView.smoothScrollBy(0, -Settings.SCROLL_ON_TOP_THRESHOLD);
        mBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void resetScrollPos() {
        scrollPos = 0;
        mBinding.recyclerView.scrollToPosition(0);
        mBinding.swipeRefresh.setEnabled(true);
    }

    @Override
    public int getScrollPosition() {
        return 0;
    }
    @Override
    public void setScrollPos(int scrollPos) {
    }
}
