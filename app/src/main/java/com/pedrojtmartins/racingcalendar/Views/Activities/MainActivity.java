package com.pedrojtmartins.racingcalendar.Views.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pedrojtmartins.racingcalendar.Adapters.Pagers.MainPagerAdapter;
import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Helpers.SnackBarHelper;
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

        mViewModel.updatedFromServer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackBarHelper.display(mBinding.mainContent, R.string.dataUpdated);
            }
        });
    }
    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);
    }
    private void initViewPager() {
        MainPagerAdapter pageAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources());
        mBinding.viewPager.setAdapter(pageAdapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewPager);

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    mBinding.fab.hide();
                } else {
                    if (mBinding.viewPager.getCurrentItem() == 0) {
                        mBinding.fab.show();
                    }
                }
            }
        });
    }
    //endregion

    //region OnClicks
    public void onClick_SelectFavorites(View v) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivityForResult(intent, 1);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mViewModel.updateFavorites();
        }
    }
}
