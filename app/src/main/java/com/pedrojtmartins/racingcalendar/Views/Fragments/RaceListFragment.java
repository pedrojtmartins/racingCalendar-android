package com.pedrojtmartins.racingcalendar.Views.Fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.Adapters.RecyclerViews.RaceAdapter;
import com.pedrojtmartins.racingcalendar.BR;
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRecyclerViewFragment;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.FragmentListBinding;


public class RaceListFragment extends Fragment implements IRecyclerViewFragment {
    private IRaceList mIRaceList;
    private FragmentListBinding mBinding;
    private ObservableArrayList<Race> mList;

    private boolean mFavouritesOnly;
    private Series mSeries;

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
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(new RaceAdapter(R.layout.row_race, mList, getResources()));

        // When transitioning between fragments the recyclerview could be
        // initialized not on the top because of fragment recycling.
        // This forces it to the top every time.
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mBinding.recyclerView.scrollToPosition(0);
            }
        });
    }

    private void setupHeader() {
        mBinding.setVariable(BR.data, mSeries);
        mBinding.listHeader.setVisibility(View.VISIBLE);
        mBinding.listHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIRaceList.undoFragmentTransition();
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

    public void smoothScrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0);
    }
}
