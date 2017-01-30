package com.pedrojtmartins.racingcalendar.Database;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.Models.Race;
import com.pedrojtmartins.racingcalendar.Models.Series;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Pedro Martins
 * 29/01/2017
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseManagerTest {

    private Context fakeContext;
    private DatabaseManager db;

    @Before
    public void setUp() throws Exception {
        fakeContext = RuntimeEnvironment.application;
        db = DatabaseManager.getInstance(fakeContext);
    }

    @Test
    public void instanceShouldNotBeNull() throws Exception {
        assertNotNull(DatabaseManager.getInstance(fakeContext));
    }

    @Test
    public void racesShouldBeAdded() throws Exception {
        assertTrue(addFakeRaces(db) == 3);
        assertTrue(db.getRaces().size() == 3);
    }

    @Test
    public void seriesShouldBeAdded() throws Exception {
        assertTrue(addFakeSeries(db) == 3);
        assertTrue(db.getSeries().size() == 3);
    }

    @Test
    public void shouldReturnAllRaces() throws Exception {
        addFakeRaces(db);
        assertTrue(db.getRaces().size() == 3);
    }
    @Test
    public void shouldReturnRaceFromSeries() throws Exception {
        addFakeRaces(db);
        assertTrue(db.getRacesFromSeries(1).size() == 1);
    }

    @Test
    public void shouldReturnAllSeries() throws Exception {
        addFakeSeries(db);
        assertTrue(db.getSeries().size() == 3);
    }
    @Test
    public void getSeriesWithId() throws Exception {
        addFakeSeries(db);
        assertTrue(db.getSeriesWithId(1) != null);
    }

    private int addFakeRaces(DatabaseManager db) {

        ArrayList<Race> races = new ArrayList<>();
        races.add(new Race(1, 1, "name1", "track1", "location1", "date1"));
        races.add(new Race(2, 2, "name2", "track2", "location2", "date2"));
        races.add(new Race(3, 3, "name3", "track3", "location3", "date3"));

        return db.addRaces(races);
    }

    private int addFakeSeries(DatabaseManager db) {

        ArrayList<Series> series = new ArrayList<>();
        series.add(new Series(1, "series1", 1));
        series.add(new Series(2, "series2", 2));
        series.add(new Series(3, "series3", 3));

        return db.addSeries(series);
    }


}