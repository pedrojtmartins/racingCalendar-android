package com.pedrojtmartins.racingcalendar.Views.Activities;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pedrojtmartins.racingcalendar.Adapters.Pagers.MainPagerAdapter;
import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.Interfaces.Fragments.ISeriesList;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.SharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.ViewModels.MainViewModel;
import com.pedrojtmartins.racingcalendar.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements IRaceList, ISeriesList {
    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initViewModel();
        initToolBar();
        initViewPager();
    }

    private void initViewModel() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        ApiManager apiManager = new ApiManager();
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        mViewModel = new MainViewModel(dbManager, apiManager, sharedPreferencesManager);
    }
    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);
    }
    private void initViewPager() {
        MainPagerAdapter pageAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources());
        mBinding.container.setAdapter(pageAdapter);
        mBinding.tabs.setupWithViewPager(mBinding.container);
    }
    //endregion

    //region Callbacks
    @Override
    public ObservableArrayList<Race> getRacesList(boolean favouritesOnly) {
        return mViewModel.getRacesList(favouritesOnly);
    }
    @Override
    public ObservableArrayList<Series> getSeriesList() {
        return mViewModel.getSeriesList();
    }
    //endregion
}
