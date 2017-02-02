package com.pedrojtmartins.racingcalendar.ViewModels;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.Api.ApiManager;
import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.Database.DatabaseManager;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
        ApiManager apiManager = mockApiManager();

        mViewModel = new MainViewModel(dbManager, apiManager);
    }
    private ApiManager mockApiManager(){
        ApiManager apiManager = mock(ApiManager.class);
        when(apiManager.updateRaceDataAsync(new ArrayList<Race>())).thenReturn(true);
        when(apiManager.updateSeriesDataAsync(new ArrayList<Series>())).thenReturn(true);

        return apiManager;
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