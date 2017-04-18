package com.pedrojtmartins.racingcalendar.database;

import android.content.Context;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Pedro Martins
 * 29/01/2017
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DatabaseManagerTest {

    private DatabaseManager db;

    @Before
    public void setUp() throws Exception {
        db = DatabaseManager.getInstance(RuntimeEnvironment.application);
    }

//    @Test
//    public void instanceShouldNotBeNull() throws Exception {
//        assertNotNull(DatabaseManager.getInstance(fakeContext));
//    }
//
//    @Test
//    public void racesShouldBeAdded() throws Exception {
//        assertTrue(populateRaceSeriesDB(db) == 3);
//
//        ArrayList<Race> list = db.getUpcomingRaces(true);
//
//        assertTrue(list.size() == 3);
//
//        assertTrue(list.get(1).getId() == 2);
//        assertTrue(list.get(1).getSeriesId() == 2);
//        assertTrue(list.get(1).getRaceNumber() == 2);
//        assertTrue(list.get(1).getName().equals("name2"));
//        assertTrue(list.get(1).getLocation().equals("location2"));
//        assertTrue(list.get(1).getFullDate().equals("2001-01-01T08:00:00"));
//    }
//
//    @Test
//    public void seriesShouldBeAdded() throws Exception {
//        assertTrue(addFakeSeries(db) == 3);
//        assertTrue(db.getSeries().size() == 3);
//    }
//
//    @Test
//    public void shouldReturnAllRaces() throws Exception {
//        populateRaceSeriesDB(db);
//        assertTrue(db.getUpcomingRaces(false).size() == 3);
//    }
//
//    @Test
//    public void shouldReturnAllSeries() throws Exception {
//        addFakeSeries(db);
//        assertTrue(db.getSeries().size() == 3);
//    }
//
//    @Test
//    public void getSeriesWithId() throws Exception {
//        addFakeSeries(db);
//        assertTrue(db.getSeriesWithId(1) != null);
//    }
//
//    @Test
//    public void getRacesShouldReturnOrderedByDateList() throws Exception {
//        ArrayList<Race> races = new ArrayList<>();
//        races.add(new Race(2, 2, 2, "name2", "location2", "2000-01-02T08:00:00", ""));
//        races.add(new Race(1, 1, 1, "name1", "location1", "2000-01-01T08:00:00", ""));
//        races.add(new Race(3, 3, 3, "name3", "location3", "2000-01-03T08:00:00", ""));
//        db.addRaces(races);
//
//        ArrayList<Race> list = db.getUpcomingRaces(false);
//        assertTrue(list.get(0).getFullDate().startsWith("2000-01-01"));
//        assertTrue(list.get(1).getFullDate().startsWith("2000-01-02"));
//        assertTrue(list.get(2).getFullDate().startsWith("2000-01-03"));
//    }
//
//    private int populateRaceSeriesDB(DatabaseManager db) {
//
//        ArrayList<Race> races = new ArrayList<>();
//        races.add(new Race(1, 1, 1, "name1", "location1", "2000-01-01T08:00:00", ""));
//        races.add(new Race(2, 2, 2, "name2", "location2", "2001-01-01T08:00:00", ""));
//        races.add(new Race(3, 3, 3, "name3", "location3", "2002-01-01T08:00:00", ""));
//
//        return db.addRaces(races);
//    }
//
//    private int addFakeSeries(DatabaseManager db) {
//
//        ArrayList<Series> series = new ArrayList<>();
//        series.add(new Series(1, "series1", 1, false, 1, 2));
//        series.add(new Series(2, "series2", 2, false, 1, 2));
//        series.add(new Series(3, "series3", 3, false, 1, 2));
//
//        return db.addSeries(series);
//    }


    //region Notifications
    private int addNotifications() {
        ArrayList<RCNotification> list = new ArrayList<>();
        list.add(new RCNotification(1, "2017-01-12T00:00:00", 5));
        list.add(new RCNotification(2, "2017-10-17T22:30:00", 15));
        list.add(new RCNotification(3, "2017-01-12T13:00:00", 60));

        return db.addNotifications(list);
    }

    //region Add Tests
    @Test
    public void addNotificationsShouldCheckNulls() throws Exception {
        assertTrue(db.addNotifications(null) == 0);

        ArrayList<RCNotification> rcNotifications = new ArrayList<>();
        rcNotifications.add(null);
        assertTrue(db.addNotifications(rcNotifications) == 0);
    }

    @Test
    public void addNotificationsShouldCheckEmpties() throws Exception {
        assertTrue(db.addNotifications(new ArrayList<RCNotification>()) == 0);
    }

    @Test
    public void addNotificationsShouldAddValues() throws Exception {
        assertTrue(addNotifications() == 3);
    }
    //endregion

    //region Get tests
    @Test
    public void getNotificationsShouldReturnEmptyIfDBEmpty() throws Exception {
        ArrayList<RCNotification> values = db.getNotifications();
        assertNotNull(values);
        assertTrue(values.size() == 0);
    }

    @Test
    public void getNotificationsShouldReturnValues() throws Exception {
        addNotifications();

        ArrayList<RCNotification> values = db.getNotifications();
        assertNotNull(values);
        assertTrue(values.size() == 3);
    }

    @Test
    public void getNotificationsShouldReturnDateOrderedValues() throws Exception {
        addNotifications();

        ArrayList<RCNotification> values = db.getNotifications();
        assertTrue(values.get(0).id == 1);
        assertTrue(values.get(1).id == 3);
        assertTrue(values.get(2).id == 2);
    }
    //endregion

    //region Delete Tests

    @Test
    public void removeNotificationsShouldIgnoreEmptyAndNulls() throws Exception {
        addNotifications();

        assertTrue(db.removeNotifications(null) == 0);

        ArrayList<RCNotification> empty = new ArrayList<>();
        assertTrue(db.removeNotifications(empty) == 0);

        ArrayList<RCNotification> withNull = new ArrayList<>();
        withNull.add(null);
        assertTrue(db.removeNotifications(withNull) == 0);
    }

    @Test
    public void removeNotificationsShouldRemove() throws Exception {
        addNotifications();
        ArrayList<RCNotification> notifications = db.getNotifications();
        assertTrue(db.removeNotifications(notifications) == 3);
    }

    //endregion
    //endregion


}