package com.pedrojtmartins.racingcalendar.ViewModels;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;


/**
 * Pedro Martins
 * 29/01/2017
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainViewModelTest {

    private MainViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        Context context = RuntimeEnvironment.application;
        DatabaseManager dbManager = DatabaseManager.getInstance(context);
        mViewModel = new MainViewModel(dbManager);
    }

    @Test
    public void raceListShouldNotBeNull() {
        assertNotNull(mViewModel.getRaceList());
    }
}