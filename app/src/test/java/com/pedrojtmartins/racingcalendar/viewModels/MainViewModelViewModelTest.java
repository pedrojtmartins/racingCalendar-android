package com.pedrojtmartins.racingcalendar.viewModels;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.api.APIManager;
import com.pedrojtmartins.racingcalendar.database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.sharedPreferences.SharedPreferencesManager;

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

        APIManager apiManager = mock(APIManager.class);
        mViewModel = new MainViewModel(DatabaseManager.getInstance(context),
                apiManager,
                mock(SharedPreferencesManager.class));
        mViewModel.initialize();
    }

    @Test
    public void raceListShouldNotBeNull() {
        assertNotNull(mViewModel.getRacesList(true));
    }

    @Test
    public void seriesListShouldNotBeNull() {
        assertNotNull(mViewModel.getSeriesList());
    }
}