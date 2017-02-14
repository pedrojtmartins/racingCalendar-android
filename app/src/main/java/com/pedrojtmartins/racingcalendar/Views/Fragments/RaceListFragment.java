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
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.databinding.FragmentListFabBinding;

public class RaceListFragment extends Fragment {
    private IRaceList mIRaceList;
    private FragmentListFabBinding mBinding;
    private ObservableArrayList<Race> mList;
    private boolean mFavouritesOnly;

    public Fragment newInstance(final boolean favouritesOnly) {
        RaceListFragment f = new RaceListFragment();
        f.mFavouritesOnly = favouritesOnly;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_fab, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // By doing it this way we guarantee that databinding will work properly
        // when some change is made to the mList in the viewModel
        mList = mIRaceList.getRacesList(mFavouritesOnly);

        // Since we are specifying the item layout here and using databinding
        // we will be able to have different layouts easily without changing the adapter.
        // To achieve that the fragment will need to be aware of the selected layout.
        // We can use shared preferences for that purpose for example.
        //TODO implement multiple layout selection capabilities
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setAdapter(new RaceAdapter(R.layout.row_race, mList));

        mBinding.fab.setVisibility(mFavouritesOnly ? View.VISIBLE : View.GONE);
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
}
