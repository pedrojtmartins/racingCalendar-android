package com.pedrojtmartins.racingcalendar.Views.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.ViewModels.MainViewModel;
import com.pedrojtmartins.racingcalendar.Views.Fragments.RaceListFragment;
import com.pedrojtmartins.racingcalendar.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements IRaceList {

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = new MainViewModel();

        initListFragment();
    }

    private void initListFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RaceListFragment fragment = new RaceListFragment();
        fragmentTransaction.add(R.id.fragment1_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public ObservableArrayList<Race> getList() {
        return mViewModel.getRaceList();
    }
}
