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
        assertTrue(populateRaceSeriesDB(db) == 3);

        ArrayList<Race> list = db.getUpcomingRaces(true);

        assertTrue(list.size() == 3);

        assertTrue(list.get(1).getId() == 2);
        assertTrue(list.get(1).getSeriesId() == 2);
        assertTrue(list.get(1).getRaceNumber() == 2);
        assertTrue(list.get(1).getName().equals("name2"));
        assertTrue(list.get(1).getLocation().equals("location2"));
        assertTrue(list.get(1).getFullDate().equals("2001-01-01T08:00:00"));
    }

    @Test
    public void seriesShouldBeAdded() throws Exception {
        assertTrue(addFakeSeries(db) == 3);
        assertTrue(db.getSeries().size() == 3);
    }

    @Test
    public void shouldReturnAllRaces() throws Exception {
        populateRaceSeriesDB(db);
        assertTrue(db.getUpcomingRaces(true).size() == 3);
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

    @Test
    public void getRacesShouldReturnOrderedByDateList() throws Exception {
        ArrayList<Race> races = new ArrayList<>();
        races.add(new Race(2, 2, 2, "name2", "location2", "2000-01-02T08:00:00", ""));
        races.add(new Race(1, 1, 1, "name1", "location1", "2000-01-01T08:00:00", ""));
        races.add(new Race(3, 3, 3, "name3", "location3", "2000-01-03T08:00:00", ""));
        db.addRaces(races);

        ArrayList<Race> list = db.getUpcomingRaces(true);
        assertTrue(list.get(0).getFullDate().startsWith("2000-01-01"));
        assertTrue(list.get(1).getFullDate().startsWith("2000-01-02"));
        assertTrue(list.get(2).getFullDate().startsWith("2000-01-03"));
    }
    private int populateRaceSeriesDB(DatabaseManager db) {

        ArrayList<Race> races = new ArrayList<>();
        races.add(new Race(1, 1, 1, "name1", "location1", "2000-01-01T08:00:00", ""));
        races.add(new Race(2, 2, 2, "name2", "location2", "2001-01-01T08:00:00", ""));
        races.add(new Race(3, 3, 3, "name3", "location3", "2002-01-01T08:00:00", ""));

        return db.addRaces(races);
    }

    private int addFakeSeries(DatabaseManager db) {

        ArrayList<Series> series = new ArrayList<>();
        series.add(new Series(1, "series1", 1, false));
        series.add(new Series(2, "series2", 2, false));
        series.add(new Series(3, "series3", 3, false));

        return db.addSeries(series);
    }


}