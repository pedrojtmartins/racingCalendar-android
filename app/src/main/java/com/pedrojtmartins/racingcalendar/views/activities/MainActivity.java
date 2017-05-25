package com.pedrojtmartins.racingcalendar.views.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.adapters.pagers.MainPagerAdapter;
import com.pedrojtmartins.racingcalendar.admob.AdmobHelper;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmManager;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.databinding.ActivityMainBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.helpers.IntentHelper;
import com.pedrojtmartins.racingcalendar.helpers.ParsingHelper;
import com.pedrojtmartins.racingcalendar.helpers.SettingsHelper;
import com.pedrojtmartins.racingcalendar.helpers.SnackBarHelper;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesList;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.viewModels.MainViewModel;


public class MainActivity extends AppCompatActivity implements IRaceList, ISeriesList, ISeriesCallback {
    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;
    private MainPagerAdapter mPageAdapter;
    private AdmobHelper mAdmobHelper;

    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SettingsHelper.detectSystemSettings(this);

        startAdmob();
        initViewModel();
        initToolBar();
        initViewPager();

        checkDatabaseDataCount();

//        showReleaseNotes();
    }

//    private void showReleaseNotes() {
//        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
//        boolean notesShown = spManager.getReleaseNotes1Shown();
//        if (!notesShown) {
//            AlertDialogHelper.displayOkDialog(this, R.string.release_notes_1);
//            spManager.setReleaseNotes1Seen();
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.recheckUpdates();


    }

    private void startAdmob() {
        mAdmobHelper = AdmobHelper.getInstance();
        mAdmobHelper.showBannerAd(getApplicationContext(), getResources(), mBinding.adView);
        mAdmobHelper.readyInterstitialAd(getApplicationContext(), getResources());
    }

    private void showNotificationAd() {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        int count = spManager.getNotificationAdsShownCount();
        spManager.notificationAdShown();

        mAdmobHelper = AdmobHelper.getInstance();
        mAdmobHelper.showInterstitialAd(getApplicationContext(), getResources(), count, null);
    }

    private boolean showUrlAd(final String url) {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        int count = spManager.getUrlAdsShownCount();
        spManager.urlAdShown();

        mAdmobHelper = AdmobHelper.getInstance();
        return mAdmobHelper.showInterstitialAd(
                getApplicationContext(),
                getResources(),
                count,
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        openUrl(url);
                        return true;
                    }
                }));
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
                String seriesAdded = mViewModel.updatedFromServerNewSeries;
                if (seriesAdded == null || seriesAdded.isEmpty()) {
                    // Series were only updated
                    SnackBarHelper.display(mBinding.mainContent, R.string.dataUpdated);
                } else {
                    // New series added
                    AlertDialogHelper.displayNewSeriesDialog(
                            mBinding.mainContent.getContext(),
                            getResources(),
                            R.string.newSeries,
                            seriesAdded);
                }
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

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.action_rate:
                Intent intent = AppVersionHelper.getGooglePlayIntent(getPackageName(), getPackageManager());
                startActivity(intent);
                break;

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
        if (undoFragmentTransition())
            return;

        // In case the active scroll view is not on top, move it up and return
        int currTab = mBinding.viewPager.getCurrentItem();
        if (!mPageAdapter.isOnTop(currTab)) {
            mPageAdapter.smoothScrollToTop(currTab);
            return;
        }

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
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_SERIES);
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
    public boolean updateAlarm(final Race race, boolean active) {
        if (!active) {
            if (mViewModel.removeNotification(race)) {
                race.setIsAlarmSet(false);
                FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_REMOVE_NOTIFICATION);
            }
            return true;
        }

        // TODO: 23/05/2017 this needs to take dates count into consideration
        int valid = RCAlarmManager.isValid(race.getFullDate(0));
        switch (valid) {
            case -1:
                SnackBarHelper.display(mBinding.mainContent, R.string.raceToday);
                return false;

            case -2:
                SnackBarHelper.display(mBinding.mainContent, R.string.alarmInPast);
                return false;
        }

        // TODO: 23/05/2017 this needs to take dates count into consideration
        if (race.hasDateOnly(0)) {
            updateAlarm(race, 0);
            return true;
        } else {

            final SharedPreferencesManager spManager = new SharedPreferencesManager(this);
            final RCSettings rcSettings = spManager.getSettings();
            if (rcSettings.notificationsRemember) {
                // Settings are stored. Use them
                updateAlarm(race, ParsingHelper.stringToInt(rcSettings.getNotificationMinutesBefore()));
                return true;
            } else {
                // Settings are not stored. Let's ask the user what to do.
                AlertDialogHelper.displayNewNotificationDialog(
                        this,
                        getLayoutInflater(),
                        rcSettings.getNotificationMinutesBefore(),
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                int minutesBefore = ParsingHelper.stringToInt(msg.getData().getString("timeBefore", "0"));

                                if (msg.what == 2) {
                                    // User wants to remember the settings. Update the settings we have already
                                    rcSettings.notificationsRemember = true;
                                    rcSettings.setNotificationMinutesBefore(minutesBefore + "");
                                    spManager.addSettings(rcSettings.toString());
                                } else {
                                    // Keep the minutes selected but just suggest next time
                                    rcSettings.setNotificationMinutesBefore(minutesBefore + "");
                                    spManager.addSettings(rcSettings.toString());
                                }

                                updateAlarm(race, minutesBefore);
                                return true;
                            }
                        }));
            }
        }

        return true;
    }

    //endregion

    private void updateAlarm(Race race, int timeBefore) {
        RCNotification rcNotification = mViewModel.addNotification(race, timeBefore);
        if (rcNotification == null) {
            return;
        }

        rcNotification.minutesBefore = timeBefore;

        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(this, rcNotification);

        boolean result = RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);
        if (result) {
            race.setIsAlarmSet(true);
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmSet);
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_NOTIFICATION);
            showNotificationAd();
        } else {
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmNotSet);
        }
    }


    @Override
    public void openUrl(Race race) {
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_RACE_URL);

        String url = mViewModel.getFullUrl(race);
        if (!showUrlAd(url))
            openUrl(url);
    }

    @Override
    public void openUrl(Series series) {
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_SERIES_URL);
        String url = series.getUrl();
        if (!showUrlAd(url))
            openUrl(url);
    }

    private void openUrl(String url) {
        if (url == null) {
            return;
        }

        if (!IntentHelper.openUrl(this, url, mViewModel.openLinksInBrowser())) {
            //No web app found. Alert the user
            AlertDialogHelper.displayOkDialog(this, R.string.noBrowser);
        }
    }

    @Override
    public void openNotifications(Race race) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.putExtra("raceId", race.getId());
        startActivityForResult(intent, 1);
    }

    @Override
    public void loadPrevious(Series series) {
        int count = mViewModel.loadPrevious(series);
        mPageAdapter.previousItemsLoaded(mBinding.viewPager.getCurrentItem(), count);
    }

    @Override
    public void loadPrevious(boolean favouritesOnly) {
        int count = mViewModel.loadPrevious(favouritesOnly);
        mPageAdapter.previousItemsLoaded(mBinding.viewPager.getCurrentItem(), count);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mViewModel.reload();
            mPageAdapter.resetFavouritesScrollPos();
        }
    }
}
