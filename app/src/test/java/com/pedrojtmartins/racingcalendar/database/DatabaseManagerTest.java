package com.pedrojtmartins.racingcalendar.database;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.Series;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
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

    @Test
    public void instanceShouldNotBeNull() throws Exception {
        assertNotNull(DatabaseManager.getInstance(RuntimeEnvironment.application));
    }

    //
//    @Test
//    public void racesShouldBeAdded() throws Exception {
//        assertTrue(populateRaceSeriesDB(db) == 3);
//
//        ArrayList<Race> list = db.getRaces(true);
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
    @Test
    public void seriesShouldBeAdded() throws Exception {
        assertTrue(addFakeSeries() == 3);
        assertTrue(db.getSeries().size() == 3);
    }
    //
//    @Test
//    public void shouldReturnAllRaces() throws Exception {
//        populateRaceSeriesDB(db);
//        assertTrue(db.getRaces(false).size() == 3);
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
//        ArrayList<Race> list = db.getRaces(false);
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
    private int addFakeSeries() {

        ArrayList<Series> series = new ArrayList<>();
        series.add(new Series(1, "series1", 2017, false, 10, 2, "", "", 0, 0));
        series.add(new Series(2, "series2", 2017, false, 10, 2, "", "", 0, 0));
        series.add(new Series(3, "series3", 2017, false, 10, 2, "", "", 0, 0));

        return db.addSeries(series);
    }


    //region NotificationsActivity
    private int addNotifications() {
        ArrayList<RCNotification> list = new ArrayList<>();
        list.add(new RCNotification(1, 1, "2099-01-12T00:00:00", 0, 5));
        list.add(new RCNotification(2, 2, "2099-10-17T22:30:00", 0, 15));
        list.add(new RCNotification(3, 3, "2099-01-12T13:00:00", 0, 60));

        return db.addNotifications(list);
    }

    private long addNotification() {
        RCNotification rcNotification = new RCNotification(1, 1, "2017-01-12T00:00:00", 0, 5);
        return db.addNotification(rcNotification);
    }

    //region Add Tests
    @Test
    public void addNotificationShouldCheckNulls() throws Exception {
        assertTrue(db.addNotification(null) == -1);
    }

    @Test
    public void addNotificationShouldAddValue() throws Exception {
        assertTrue(addNotification() > 0);
    }


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
    public void getNotificationShouldReturnNullIfInvalidId() throws Exception {
        assertNull(db.getNotification(-1));
    }

    @Test
    public void getNotificationShouldReturnNullIfNotFound() throws Exception {
        assertNull(db.getNotification(1));
    }

    @Test
    public void getNotificationShouldReturnNotificationIfValid() throws Exception {
        addNotification();

        RCNotification rcNotification = db.getNotification(1);
        assertNotNull(rcNotification);
        assertEquals(rcNotification.raceId, 1);
        assertEquals(rcNotification.time, "2017-01-12T00:00:00");
        assertEquals(rcNotification.minutesBefore, 5);
    }

    @Test
    public void getNotificationsShouldReturnEmptyIfDBEmpty() throws Exception {
        ArrayList<RCNotification> values = db.getUpcomingNotifications();
        assertNotNull(values);
        assertTrue(values.size() == 0);
    }

    @Test
    public void getNotificationsShouldReturnValues() throws Exception {
        addFakeSeries();
        addNotifications();

        ArrayList<RCNotification> values = db.getUpcomingNotifications();
        assertNotNull(values);
        assertTrue(values.size() == 3);
    }

    @Test
    public void getNotificationsShouldReturnDateOrderedValues() throws Exception {
        addNotifications();

        ArrayList<RCNotification> values = db.getUpcomingNotifications();
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
        ArrayList<RCNotification> notifications = db.getUpcomingNotifications();
        assertTrue(db.removeNotifications(notifications) == 3);
    }

    //endregion
    //endregion


}