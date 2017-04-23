package com.pedrojtmartins.racingcalendar.views.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Settings;
import com.pedrojtmartins.racingcalendar.adapters.pagers.MainPagerAdapter;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmBroadcastReceiver;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmManager;
import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.databinding.ActivityMainBinding;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.helpers.SettingsHelper;
import com.pedrojtmartins.racingcalendar.helpers.SnackBarHelper;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesList;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.viewModels.MainViewModel;


public class MainActivity extends AppCompatActivity implements IRaceList, ISeriesList, ISeriesCallback {
    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;
    private MainPagerAdapter mPageAdapter;

    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SettingsHelper.detectSystemSettings(this);

        initAdMob();
        initViewModel();
        initToolBar();
        initViewPager();

        checkDatabaseDataCount();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.recheckUpdates();
    }

    private void initAdMob() {
        if (!Settings.PRO_VERSION) {
            MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_id));
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(Settings.TEST_DEVICE_ID)
                    .build();

            mBinding.adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mBinding.adView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    // TODO: 20/04/2017 set network state listener

                }
            });
            mBinding.adView.loadAd(adRequest);
        } else {
            mBinding.adView.setVisibility(View.GONE);
        }
    }

    private void initViewModel() {
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        APIManager apiManager = new APIManager();
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        mViewModel = new MainViewModel(dbManager, apiManager, sharedPreferencesManager);

        //This will show a snackbar when the data is updated from the server
        mViewModel.updatedFromServer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackBarHelper.display(mBinding.mainContent, R.string.dataUpdated);
            }
        });

        //This will show a snackbar when a new app update is availabel
        mViewModel.newAppUpdate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackBarHelper.displayWithAction(
                        mBinding.mainContent,
                        R.string.newAppVersion,
                        R.string.update,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = AppVersionHelper.getGooglePlayIntent(getPackageName(), getPackageManager());
                                startActivity(intent);
                            }
                        },
                        Snackbar.LENGTH_INDEFINITE);
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);
    }

    private void initViewPager() {
        mPageAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources());
        mBinding.viewPager.setAdapter(mPageAdapter);

        mBinding.tabs.setupWithViewPager(mBinding.viewPager);
        mBinding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mPageAdapter.smoothScrollToTop(tab.getPosition());
            }
        });

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
                    if (mBinding.viewPager.getCurrentItem() == MainPagerAdapter.PAGE_FAVOURITES) {
                        mBinding.fab.show();
                    }
                }
            }
        });

        //In case there is no favourites in the db display the All tab by default
        DatabaseManager dbManager = DatabaseManager.getInstance(this);
        if (dbManager.getFavouritesCount() == 0) {
            mBinding.fab.hide(); // Keep in mind the page change listener won't be called
            mBinding.viewPager.setCurrentItem(MainPagerAdapter.PAGE_ALL, true);
        } else {
            mBinding.viewPager.setCurrentItem(MainPagerAdapter.PAGE_FAVOURITES, true);
        }

    }

    private void checkDatabaseDataCount() {
        // We only need to check if series table is populated.
        // The races table will be in the same state.
        // In case we have no series show a snackbar to warn the user
        // that the data is being downloaded from the server.
        ObservableArrayList<Series> seriesList = mViewModel.getSeriesList();
        if (seriesList == null || seriesList.size() == 0) {
            SnackBarHelper.display(mBinding.mainContent, R.string.firstTimeDataDownload, Snackbar.LENGTH_INDEFINITE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_notifications:
                startActivityForResult(new Intent(this, NotificationsActivity.class), 1);
                break;

            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;

//            case R.id.action_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
//                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    //endregion

    //region System callbacks
    @Override
    public void onBackPressed() {
        // We might have some transaction in place on our fragments
        // In this case we need to go back to the original fragment
        // and continue without finishing the activity.
        if (!undoFragmentTransition())
            super.onBackPressed();
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
    public ObservableArrayList<Race> getRacesListBySeries(int seriesId) {
        return mViewModel.getRacesList(seriesId);
    }

    @Override
    public boolean undoFragmentTransition() {
        return mPageAdapter.undoFragmentReplace(mBinding.viewPager.getCurrentItem());
    }

    @Override
    public ObservableArrayList<Series> getSeriesList() {
        return mViewModel.getSeriesList();
    }

    @Override
    public void displayRacesFromSeries(Series seriesId) {
        mPageAdapter.replaceSeriesWithRaces(seriesId);
//        mBinding.viewPager.setCurrentItem(MainPagerAdapter.PAGE_SERIES);
    }
    //endregion

    //region Alarms
    public boolean updateAlarm(Race race, boolean active) {
        if (!active) {
            mViewModel.removeNotification(race);
            return true;
        }

        int valid = RCAlarmManager.isValid(race.getFullDate());
        switch (valid) {
            case -1:
                SnackBarHelper.display(mBinding.mainContent, R.string.raceToday);
                return false;

            case -2:
                SnackBarHelper.display(mBinding.mainContent, R.string.alarmInPast);
                return false;
        }

        RCNotification rcNotification = mViewModel.addNotification(race, 0);
        if (rcNotification == null)
            return false;

        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(this, rcNotification);

        boolean result = RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);
        if (result) {
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmSet);
        } else {
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmNotSet);
        }

        return result;
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mViewModel.reload();
        }
    }
}
