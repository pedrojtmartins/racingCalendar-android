package com.pedrojtmartins.racingcalendar.views.fragments;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.adapters.recyclerViews.RaceAdapter;
import com.pedrojtmartins.racingcalendar.databinding.FragmentListBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRecyclerViewFragment;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;


public class RaceListFragment extends Fragment implements IRecyclerViewFragment {
    private IRaceList mIRaceList;
    private FragmentListBinding mBinding;
    private ObservableArrayList<Race> mList;
    private int firstActiveRaceIndex = -1;

    private boolean miniLayout;

    private boolean mFavouritesOnly;
    private Series mSeries;

    public Fragment newInstance(final boolean favouritesOnly, boolean miniLayout) {
        RaceListFragment f = new RaceListFragment();
        f.mFavouritesOnly = favouritesOnly;
        f.miniLayout = miniLayout;

        return f;
    }

    public Fragment newInstance(final Series series, boolean miniLayout) {
        RaceListFragment f = new RaceListFragment();
        f.mSeries = series;
        f.miniLayout = miniLayout;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                int originalSize = mList != null ? mList.size() : 0;

                if (mSeries != null) {
                    mIRaceList.loadPrevious(mSeries);
                } else {
                    mIRaceList.loadPrevious(mFavouritesOnly);
                }

                // After we load the previous rows
                // disable the swiping functionality
                mBinding.swipeRefresh.setEnabled(false);

                // We need to keep track of the first active race.
                // This will be needed in case the user changes the layout settings
                // and previous races are loaded. If we don't keep track of this
                // when the layout is updated the first visible item will be the first one in the list.

                int newSize = mList != null ? mList.size() : 0;
                firstActiveRaceIndex = newSize - originalSize;
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

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(new RaceAdapter(
                R.layout.row_race2,
                mList,
                mIRaceList,
                getResources(),
                miniLayout));

        // When transitioning between fragments the recyclerview could be
        // initialized not on the top because of fragment recycling.
        // This forces it to the top every time.
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                scrollToTop();
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
        if (firstActiveRaceIndex > 0) {
            mBinding.recyclerView.smoothScrollToPosition(firstActiveRaceIndex);
        } else {
            mBinding.recyclerView.smoothScrollToPosition(0);
        }
    }

    private void scrollToTop() {
        if (firstActiveRaceIndex > 0) {
            mBinding.recyclerView.scrollToPosition(firstActiveRaceIndex);
        } else {
            mBinding.recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public boolean isOnTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mBinding.recyclerView.getLayoutManager();
        if (layoutManager == null)
            return true;

        int firstVisiblePos = layoutManager.findFirstVisibleItemPosition();
        int activeOffset = firstActiveRaceIndex > 0 ? firstActiveRaceIndex : 0;
        int offset = Math.abs(activeOffset - firstVisiblePos);

        int maxOffset = 0;
        if (miniLayout) {
            maxOffset = FirebaseManager.getIntRemoteConfig(FirebaseManager.REMOTE_CONFIG_AUTO_SCROLL_MINI_OFFSET);
        } else {
            maxOffset = FirebaseManager.getIntRemoteConfig(FirebaseManager.REMOTE_CONFIG_AUTO_SCROLL_NORMAL_OFFSET);
        }
        return offset <= maxOffset;
    }

    @Override
    public void itemsReloaded(int count) {
        int threshold = FirebaseManager.getIntRemoteConfig(FirebaseManager.REMOTE_CONFIG_AUTO_SCROLL_THRESHOLD);
        mBinding.recyclerView.smoothScrollBy(0, -threshold);
        mBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void resetScrollPosToItem0() {
        mBinding.recyclerView.scrollToPosition(0);
        mBinding.swipeRefresh.setEnabled(true);
    }

    /**
     * Updates the layout if it needs was change since the last check.
     * It does nothing otherwise.
     *
     * @param miniLayout layout to load
     */
    public void updateLayoutIfNeeded(boolean miniLayout) {
        // Probably use a Application singleton to prevent the isAdded returning false??
        if (this.miniLayout == miniLayout || !isAdded()) {
            return;
        }

        this.miniLayout = miniLayout;

        // Update layout
        mBinding.recyclerView.setAdapter(new RaceAdapter(
                R.layout.row_race2,
                mList,
                mIRaceList,
                getResources(),
                miniLayout));

        if (firstActiveRaceIndex > 0) {
            mBinding.recyclerView.scrollToPosition(firstActiveRaceIndex);
        }
    }
}
