package com.pedrojtmartins.racingcalendar.ViewModels;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.SharedPreferences.SharedPreferencesManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


/**
 * Pedro Martins
 * 29/01/2017
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainViewModelViewModelTest {


    //    private SharedPreferencesManager sharedPreferencesManager;
//    private DatabaseManager dbManager;
    private MainViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application;

        ApiManager apiManager = mock(ApiManager.class);
        mViewModel = new MainViewModel(DatabaseManager.getInstance(context),
                apiManager,
                mock(SharedPreferencesManager.class));
    }

    @Test
    public void raceListShouldNotBeNull() {
        assertNotNull(mViewModel.getRaceList());
    }

    @Test
    public void seriesListShouldNotBeNull() {
        assertNotNull(mViewModel.getSeriesList());
    }
}