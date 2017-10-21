package com.pedrojtmartins.racingcalendar.views.activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar._settings.Constants;
import com.pedrojtmartins.racingcalendar.adapters.pagers.MainPagerAdapter;
import com.pedrojtmartins.racingcalendar.admob.AdmobHelper;
import com.pedrojtmartins.racingcalendar.alarms.RCAlarmManager;
import com.pedrojtmartins.racingcalendar.alertDialog.AlertDialogHelper;
import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.contentProviders.CalendarProvider;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.databinding.ActivityMainBinding;
import com.pedrojtmartins.racingcalendar.firebase.FirebaseManager;
import com.pedrojtmartins.racingcalendar.helpers.AppVersionHelper;
import com.pedrojtmartins.racingcalendar.helpers.IntentHelper;
import com.pedrojtmartins.racingcalendar.helpers.InternetConnectionTest;
import com.pedrojtmartins.racingcalendar.helpers.ParsingHelper;
import com.pedrojtmartins.racingcalendar.helpers.SettingsHelper;
import com.pedrojtmartins.racingcalendar.helpers.SnackBarHelper;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.IRaceList;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesCallback;
import com.pedrojtmartins.racingcalendar.interfaces.fragments.ISeriesList;
import com.pedrojtmartins.racingcalendar.models.InternalCalendars;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.RCSettings;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;
import com.pedrojtmartins.racingcalendar.notifications.RCNotificationManager;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;
import com.pedrojtmartins.racingcalendar.userActivity.UserActivityManager;
import com.pedrojtmartins.racingcalendar.viewModels.MainViewModel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements IRaceList, ISeriesList, ISeriesCallback {

    private static final int ACTIVITY_RESULT_NOTIFICATIONS = 1;
    private static final int ACTIVITY_RESULT_SETTINGS = 2;

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;
    private MainPagerAdapter mPageAdapter;

    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SettingsHelper.detectSystemSettings(this);
        createNotificationChannels();

        startAdmob();
        initViewModel();
        initToolBar();
        initViewPager();

        showReleaseNotes();
        checkRateRequestStatus();
    }

    private void createNotificationChannels() {
        RCNotificationManager.createChannels(
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE),
                getResources());
    }

    private void showReleaseNotes() {
        if (mViewModel.displayReleaseNotes()) {
            AlertDialogHelper.displayReleaseNotesDialog(this, R.string.releaseNotesMsg, new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    mViewModel.releaseNotesDismissed();
                    return true;
                }
            }));
        }
    }

    private void checkRateRequestStatus() {
        UserActivityManager manager = UserActivityManager.getInstance();
        DatabaseManager databaseManager = DatabaseManager.getInstance(this);

        manager.addAppStart(databaseManager);
        boolean isReadyToRequestRate = manager.isReadyToRequestRate(databaseManager);
        mViewModel.setReadyToRequestRate(isReadyToRequestRate);
    }

    //region Rate request
//    private void requestRate() {
//        if (!mViewModel.isReadyToRequestRate())
//            return;
//
//        FirebaseManager.logEvent(this, FirebaseManager.EVENT_REQUEST_RATE);
//        AlertDialogHelper.displayYesNoDialog(this, R.string.areYouHappyWithApp, R.string.yes, R.string.no, false, new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                if (message.what == 1) {
//                    requestRatePositive();
//                } else {
//                    requestRateNegative();
//                }
//                return true;
//            }
//        }));
//    }

//    private void requestRatePositive() {
//        FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_LIKES_APP);
//        AlertDialogHelper.displayYesNoDialog(this, R.string.requestRate, R.string.yes, R.string.no, false, new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                requestRatePositiveResult(message.what);
//                return true;
//            }
//        }));
//    }
//
//    private void requestRatePositiveResult(int res) {
//        switch (res) {
//            case 1:
//                FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_LIKES_Y_RATES_APP);
//                UserActivityManager.addUserActivity(DatabaseManager.getInstance(this), true, true);
//                startActivity(AppVersionHelper.getGooglePlayIntent(getPackageName(), getPackageManager()));
//                break;
//
//            case 0:
//                FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_LIKES_N_RATES_APP);
//                UserActivityManager.addUserActivity(DatabaseManager.getInstance(this), true, false);
//                break;
//        }
//    }

//    private void requestRateNegative() {
//        FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_DISLIKES_APP);
//        AlertDialogHelper.displayYesNoDialog(this, R.string.contactForSupport, R.string.yes, R.string.no, false, new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message message) {
//                requestRateNegativeResult(message.what);
//                return true;
//            }
//        }));
//    }
//
//    private void requestRateNegativeResult(int res) {
//        UserActivityManager.addUserActivity(DatabaseManager.getInstance(this), true, false);
//
//        switch (res) {
//            case 1:
//                FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_DISLIKES_Y_SENDS_MAIL);
//                IntentHelper.sendFeedback(this);
//                break;
//
//            case 0:
//                FirebaseManager.logEvent(this, FirebaseManager.EVENT_USER_DISLIKES_N_SENDS_MAIL);
//                break;
//        }
//    }
    //endregion

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.recheckUpdates();
    }

    private void startAdmob() {
        AdmobHelper admobHelper = AdmobHelper.getInstance();
        admobHelper.showBannerAd(getApplicationContext(), getResources(), mBinding.adView);
        admobHelper.readyInterstitialAd(getApplicationContext(), getResources());
    }

    private boolean showInterstitialAd(boolean always, Handler.Callback callback) {
        int count = 0;
        if (!always) {
            SharedPreferencesManager spManager = new SharedPreferencesManager(this);
            count = spManager.getNotificationAdsShownCount();
            spManager.notificationAdShown();
        }

        return AdmobHelper.getInstance()
                .showInterstitialAd(getApplicationContext(), getResources(), count, always, callback);
    }

    private boolean showUrlAd(final String url) {
        SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        int count = spManager.getUrlAdsShownCount();
        spManager.urlAdShown();

        return AdmobHelper.getInstance()
                .showInterstitialAd(
                        getApplicationContext(),
                        getResources(),
                        count,
                        false,
                        new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                openUrl(url);
                                return true;
                            }
                        });
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
                if (seriesAdded != null && !seriesAdded.isEmpty()) {
                    // New series added
                    AlertDialogHelper.displayNewSeriesDialog(
                            mBinding.mainContent.getContext(),
                            getResources(),
                            R.string.newSeries,
                            seriesAdded);
                }

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

        mViewModel.firstInitialization.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get())
                    checkInternetConnection();
            }
        });

        mViewModel.startingDownload.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SnackBarHelper.display(mBinding.mainContent, R.string.downloadingData, Snackbar.LENGTH_INDEFINITE);
            }
        });

        mViewModel.initialize();
    }

    private void checkInternetConnection() {
        // This will only happen on the first initialization
        new InternetConnectionTest(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0)
                    noInternetConnection();

                return false;
            }
        }).execute();
    }

    private void noInternetConnection() {
        AlertDialogHelper.displayOkDialog(this, R.string.notInternetFirstInit, new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                onBackPressed();
                return false;
            }
        }));
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.toolbar);
    }

    private void initViewPager() {
        final SharedPreferencesManager spManager = new SharedPreferencesManager(this);
        final RCSettings rcSettings = spManager.getSettings();

        mPageAdapter = new MainPagerAdapter(getSupportFragmentManager(), getResources(), rcSettings);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_remove_ads:


            case R.id.action_notifications:
                startActivityForResult(new Intent(this, NotificationsActivity.class), ACTIVITY_RESULT_NOTIFICATIONS);
                break;

            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;

            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), ACTIVITY_RESULT_SETTINGS);
                break;

            case R.id.action_rate:
                Intent intent = AppVersionHelper.getGooglePlayIntent(getPackageName(), getPackageManager());
                startActivity(intent);
                break;

            case R.id.action_feedback:
                IntentHelper.sendFeedback(this);
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
        if (undoFragmentTransition()) {
            return;
        }

        // Are we are ready to ask for a review?
        if (mViewModel.isReadyToRequestRate()) {
            UserActivityManager.getInstance().requestRate(this);
            return;
        }

        // In case the active scroll view is not on top, move it up and return
        int currTab = mBinding.viewPager.getCurrentItem();
        if (!mPageAdapter.isOnTop(currTab)) {
            mPageAdapter.smoothScrollToTop(currTab);

            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_BACK_SCROLL);
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
    }
    //endregion

    //region Alarms
    public boolean updateAlarm(final Race race, boolean active, final int index) {
        if (!active) {
            if (mViewModel.removeNotification(race, index)) {
                race.setIsAlarmSet(index, false);
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

        int minutesBefore = getMinutesBefore(false, race, index);
        if (minutesBefore == -1) {
            //This means the user must choose still. After it is chosen the rest of the process will be run.
            return true;
        }

        updateAlarm(race, minutesBefore, index);
        return true;
    }

    public boolean updateAlarmForAllRaces(final Race race, final ArrayList<Race> list) {
        int minutesBefore = getMinutesBefore(true, race, 0, list);
        if (minutesBefore == -1) {
            //This means the user must choose still. After it is chosen the rest of the process will be run.
            return true;
        }

        updateAlarmForAllRaces(race, minutesBefore, list);
        return true;
    }

    private boolean updateAlarmForAllRaces(final Race race, final int minutesBefore, ArrayList<Race> list) {
        updateAlarmForAllRaces(race.getSeriesId(), minutesBefore, list);
        return true;
    }

    public int getMinutesBefore(final boolean forAllRaces, final Race race, final int index) {
        return getMinutesBefore(forAllRaces, race, index, null);
    }

    public int getMinutesBefore(final boolean forAllRaces, final Race race, final int index, final ArrayList<Race> list) {
        if (race.hasDateOnly(0)) {
            return 0;
        } else {
            final SharedPreferencesManager spManager = new SharedPreferencesManager(this);
            final RCSettings rcSettings = spManager.getSettings();

            if (rcSettings.notificationsRemember) {
                return ParsingHelper.stringToInt(rcSettings.getNotificationMinutesBefore());
            } else {
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
                                }

                                rcSettings.setNotificationMinutesBefore(minutesBefore + "");
                                spManager.addSettings(rcSettings);

                                if (forAllRaces) {
                                    updateAlarmForAllRaces(race, minutesBefore, list);
                                } else {
                                    updateAlarm(race, minutesBefore, index);
                                }

                                return true;
                            }
                        }));

                return -1;
            }
        }
    }

    //endregion
    private void updateAlarmForAllRaces(final int seriesId, final int timeBefore, ArrayList<Race> raceList) {
        ArrayList<RCNotification> list = mViewModel.addNotifications(seriesId, timeBefore);
        if (list == null || list.isEmpty()) {
            return;
        }

        int alarmsNotSet = 0;
        for (RCNotification notification : list) {
            boolean alarmSet = setAlarm(notification, true);
            alarmsNotSet += alarmSet ? 0 : 1;

            // Find the correct race and set it's alarm state to the right one
            for (Race r : raceList) {
                if (r.getId() == notification.raceId) {
                    r.setIsAlarmSet(notification.timeIndex, true); //This is used to update the series races view
                    break;
                }
            }
        }

        if (alarmsNotSet == 0) {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_SERIES_NOTIFICATION);
            showInterstitialAd(true, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    SnackBarHelper.display(mBinding.mainContent, R.string.alarmsSet);
                    return true;
                }
            });
        } else {
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmsNotSet);
        }
    }

    private void updateAlarm(final Race race, final int timeBefore, final int index) {
        RCNotification rcNotification = mViewModel.addNotification(race, timeBefore, index);
        if (rcNotification == null) {
            return;
        }

        race.setIsAlarmSet(index, true); //This is used to update the series races view
        setAlarm(rcNotification, false);
    }

    private boolean setAlarm(RCNotification rcNotification, boolean isMulti) {
        final AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pendingIntent = RCAlarmManager.generatePendingIntent(this, rcNotification);

        boolean result = RCAlarmManager.setAlarm(am, rcNotification, pendingIntent);

        if (isMulti)
            return result;

        if (result) {
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_SET_NOTIFICATION);
            showInterstitialAd(false, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    SnackBarHelper.display(mBinding.mainContent, R.string.alarmSet);
                    return true;
                }
            });
        } else {
            SnackBarHelper.display(mBinding.mainContent, R.string.alarmNotSet);
        }

        return result;
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

    @Override
    public void openResults(Race race) {
        if (race == null)
            return;

        final Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("seriesId", race.getSeriesId());
        intent.putExtra("raceId", race.getId());
        intent.putExtra("raceNum", race.getRaceNumber());
        intent.putExtra("seriesName", race.getSeriesName());
        intent.putExtra("raceName", race.getRaceNumberString() + " - " + race.getName());

        showInterstitialAd(false,
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        startActivity(intent);
                        return false;
                    }
                });
    }

    @Override
    public void openResults(Series series) {
        if (series == null)
            return;

        final Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("seriesId", series.getId());
        intent.putExtra("seriesName", series.getName());

        showInterstitialAd(false,
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        startActivity(intent);
                        return false;
                    }
                });
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
    public void openNotifications(Race race, int index) {
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_OPEN_NOTIFICATION);
        Intent intent = new Intent(this, NotificationsActivity.class);
        intent.putExtra("raceId", race.getId());
        intent.putExtra("dateIndex", index);
        startActivityForResult(intent, 1);
    }

    @Override
    public void loadPrevious(Series series) {
        int count = mViewModel.loadPrevious(series);
        mPageAdapter.previousItemsLoaded(mBinding.viewPager.getCurrentItem(), count);

        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_LOAD_PREVIOUS_SERIES);
    }

    @Override
    public void exportToCalendar(final Race race) {
        exportToCalendar(race, -1);
    }

    private void exportToCalendar(final Race race, final int calendarId) {
        if (race == null) // This must never happen
            return;

        if (calendarId == -1) {
            mViewModel.export(race, -1);
            ArrayList<InternalCalendars> allCalendars = CalendarProvider.getAllCalendars(this);
            if (allCalendars == null) { // Permission requested, do nothing for now
                return;
            }

            if (allCalendars.isEmpty()) { // No calendars available, warn user
                AlertDialogHelper.displayOkDialog(this, R.string.noCalendarsAvailable);
                return;
            }

            AlertDialogHelper.requestCalendar(this, allCalendars, new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    int index = message.arg1;
                    if (index >= 0)
                        exportToCalendar(race, index);

                    return true;
                }
            });
            return;
        }

        mViewModel.export(race, calendarId);
        boolean exported = CalendarProvider.addRaceToCalendar(this, race, calendarId);
        if (!exported)
            // In case the permission needs to be requested
            // this function will called again from the onRequestPermissionsResult
            return;

        // If this is reached event was exported successfully
        mViewModel.exportComplete();
        FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_EXPORT_RACE_TO_CALENDAR);

        showInterstitialAd(true, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                SnackBarHelper.display(mBinding.mainContent, R.string.raceExported);
                return true;
            }
        });
    }

    @Override
    public void loadPrevious(boolean favouritesOnly) {
        int count = mViewModel.loadPrevious(favouritesOnly);
        mPageAdapter.previousItemsLoaded(mBinding.viewPager.getCurrentItem(), count);

        if (favouritesOnly)
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_LOAD_PREVIOUS_FAVOURITES);
        else
            FirebaseManager.logEvent(this, FirebaseManager.EVENT_ACTION_LOAD_PREVIOUS_ALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case ACTIVITY_RESULT_NOTIFICATIONS:
                mViewModel.reload();
                mPageAdapter.favouritesHaveChanged();
                break;

            case ACTIVITY_RESULT_SETTINGS:
                if (data.getBooleanExtra("update_layout", false)) {
                    mPageAdapter.updateLayoutsIfNeeded(new SharedPreferencesManager(this).getSettings());
                }

                if (data.getBooleanExtra("reload_series", false)) {
                    mViewModel.reload();
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
            return;

        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_READ_CALENDAR:
                exportToCalendar(mViewModel.getRaceBeingExported(), -1);
                break;

            case Constants.PERMISSION_REQUEST_WRITE_CALENDAR:
                exportToCalendar(mViewModel.getRaceBeingExported(), mViewModel.getCalendarToExportTo());
                break;
        }
    }
}
