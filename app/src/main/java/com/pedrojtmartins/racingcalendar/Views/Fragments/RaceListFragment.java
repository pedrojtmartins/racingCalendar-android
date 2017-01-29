package com.pedrojtmartins.racingcalendar.Views.Fragments;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrojtmartins.racingcalendar.Adapters.RecyclerViewAdapter;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.R;

import org.parceler.Parcels;

import java.util.ArrayList;

public class RaceListFragment extends Fragment {

    public static RaceListFragment newInstance(ArrayList<Race> items) {
        RaceListFragment fragment = new RaceListFragment();
        Bundle args = new Bundle();
        args.putParcelable("items", Parcels.wrap(items));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_race_list, container, false).getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Race> items = savedInstanceState.getParcelable("items");

//        RecyclerViewAdapter<Race> adapter = new RecyclerViewAdapter<>(R.layout.fragment_race, items);
//        RecyclerView recyclerView = (RecyclerView) getView();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
    }
}
