package com.pedrojtmartins.racingcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;

import com.google.firebase.perf.metrics.AddTrace;
import com.pedrojtmartins.racingcalendar.helpers.DateHelper;
import com.pedrojtmartins.racingcalendar.models.RCNotification;
import com.pedrojtmartins.racingcalendar.models.Race;
import com.pedrojtmartins.racingcalendar.models.Series;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Pedro Martins
 * 29/01/2017
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static DatabaseManager mDatabaseManager;

    //region Database
    private static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 4;
    //endregion

    //region Tables
    //region Table Races
    private static final String TABLE_RACES = "races";

    //region Columns
    private static final String KEY_RACE_ID = "race_id";
    private static final String KEY_RACE_SERIES_ID = "race_seriesId";
    private static final String KEY_RACE_NUMBER = "race_no";
    private static final String KEY_RACE_NAME = "race_name";
    private static final String KEY_RACE_LOCATION = "race_location";
    private static final String KEY_RACE_DATE = "race_date";
    private static final String KEY_RACE_URL = "race_url";
    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_RACES = "CREATE TABLE " + TABLE_RACES + "(" +
            KEY_RACE_ID + " INTEGER PRIMARY KEY," +
            KEY_RACE_SERIES_ID + " INTEGER," +
            KEY_RACE_NUMBER + " INTEGER," +
            KEY_RACE_NAME + " TEXT," +
            KEY_RACE_LOCATION + " TEXT," +
            KEY_RACE_DATE + " INTEGER," +
            KEY_RACE_URL + " TEXT)";
    //endregion
    //endregion

    //region Table Series
    private static final String TABLE_SERIES = "series";

    //region Columns
    private static final String KEY_SERIES_ID = "series_id";
    private static final String KEY_SERIES_NAME = "series_name";
    private static final String KEY_SERIES_YEAR = "series_year";
    private static final String KEY_SERIES_FAVOURITE = "series_favourite";
    private static final String KEY_SERIES_URL = "series_url"; //f1.com
    private static final String KEY_SERIES_PURL = "series_purl";///calendar/
    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_SERIES = "CREATE TABLE " + TABLE_SERIES + "(" +
            KEY_SERIES_ID + " INTEGER PRIMARY KEY," +
            KEY_SERIES_NAME + " TEXT," +
            KEY_SERIES_YEAR + " INTEGER," +
            KEY_SERIES_FAVOURITE + " INTEGER," +
            KEY_SERIES_URL + " TEXT," +
            KEY_SERIES_PURL + " TEXT)";
    //endregion
    //endregion

    //region Table NotificationsActivity
    private static final String TABLE_NOTIFICATIONS = "notifications";

    //region Columns

    private static final String KEY_NOTIFICATIONS_ID = "notif_id";
    private static final String KEY_NOTIFICATIONS_RACE_ID = "notif_race_id";
    private static final String KEY_NOTIFICATIONS_SERIES_ID = "notif_series_id";
    private static final String KEY_NOTIFICATIONS_TIME = "notif_time";
    private static final String KEY_NOTIFICATIONS_MINUTES_BEFORE = "notif_mins_before";
    private static final String KEY_NOTIFICATIONS_DATE_INDEX = "notif_date_index";
    private static final String KEY_NOTIFICATIONS_COMPLETED = "notif_complete";

    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE " + TABLE_NOTIFICATIONS + "(" +
            KEY_NOTIFICATIONS_ID + " INTEGER PRIMARY KEY," +
            KEY_NOTIFICATIONS_RACE_ID + " INTEGER," +
            KEY_NOTIFICATIONS_SERIES_ID + " INTEGER," +
            KEY_NOTIFICATIONS_TIME + " TEXT," +
            KEY_NOTIFICATIONS_MINUTES_BEFORE + " INTEGER," +
            KEY_NOTIFICATIONS_DATE_INDEX + " INTEGER DEFAULT 0," +
            KEY_NOTIFICATIONS_COMPLETED + " INTEGER DEFAULT 0)";
    //endregion
    //endregion
    //endregion

    /**
     * @param context Requesting context
     * @return the instance of the DatabaseManager
     */
    public static DatabaseManager getInstance(Context context) {
        if (mDatabaseManager == null)
            mDatabaseManager = new DatabaseManager(context);

        return mDatabaseManager;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RACES);
        db.execSQL(CREATE_TABLE_SERIES);
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 1) {
            db.execSQL(CREATE_TABLE_NOTIFICATIONS);
        }

        if (oldVersion <= 2) {
            db.execSQL("ALTER TABLE " + TABLE_RACES + " ADD COLUMN " +
                    KEY_RACE_URL + " TEXT");


            db.execSQL("ALTER TABLE " + TABLE_SERIES + " ADD COLUMN " +
                    KEY_SERIES_URL + " TEXT");

            db.execSQL("ALTER TABLE " + TABLE_SERIES + " ADD COLUMN " +
                    KEY_SERIES_PURL + " TEXT");
        }

        if (oldVersion <= 3) {
            db.execSQL("ALTER TABLE " + TABLE_NOTIFICATIONS + " ADD COLUMN " +
                    KEY_NOTIFICATIONS_DATE_INDEX + " INTEGER DEFAULT 0");

            db.execSQL("ALTER TABLE " + TABLE_NOTIFICATIONS + " ADD COLUMN " +
                    KEY_NOTIFICATIONS_COMPLETED + " INTEGER DEFAULT 0");
        }
    }

    private void close(SQLiteDatabase db, Cursor cursor) {
        closeCursor(cursor);
        closeDatabase(db);
    }

    private void closeDatabase(SQLiteDatabase db) {
        if (db != null && db.isOpen())
            db.close();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed())
            cursor.close();
    }

    //region Race
    private ArrayList<Race> buildRaces(Cursor cursor, boolean upcoming) {
        Race temp = null;
        ArrayList<Race> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_RACE_ID));
                int seriesId = cursor.getInt(cursor.getColumnIndex(KEY_RACE_SERIES_ID));
                int raceNo = cursor.getInt(cursor.getColumnIndex(KEY_RACE_NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(KEY_RACE_NAME));
                String location = cursor.getString(cursor.getColumnIndex(KEY_RACE_LOCATION));
                String date = cursor.getString(cursor.getColumnIndex(KEY_RACE_DATE));
                String url = cursor.getString(cursor.getColumnIndex(KEY_RACE_URL));

                String seriesName = "";
                int seriesNameId = cursor.getColumnIndex(KEY_SERIES_NAME);
                if (seriesNameId > 0) {
                    seriesName = cursor.getString(cursor.getColumnIndex(KEY_SERIES_NAME));
                    if (seriesName == null)
                        seriesName = "";
                }

                int notificationId = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_ID));
                int notificationDateIndex = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_DATE_INDEX));

                Race race = new Race(id, seriesId, raceNo, name, location, date, seriesName, url, upcoming);
                if (notificationId > 0 && temp != null && race.getId() == temp.getId()) {
                    // This means that this race already was added to the list
                    // and we only need to set its date index as an alarm
                    temp.setIsAlarmSet(notificationDateIndex, true);
                } else {
                    // New race
                    if (notificationId > 0) {
                        race.setIsAlarmSet(notificationDateIndex, true);
                    }

                    list.add(race);

                    temp = race;
                }
            } while (cursor.moveToNext());
        }

        return list;
    }

    private ContentValues createRaceContentValue(Race race) {
        ContentValues values = new ContentValues();
        values.put(KEY_RACE_ID, race.getId());
        values.put(KEY_RACE_SERIES_ID, race.getSeriesId());
        values.put(KEY_RACE_NUMBER, race.getRaceNumber());
        values.put(KEY_RACE_NAME, race.getName());
        values.put(KEY_RACE_LOCATION, race.getLocation());
        values.put(KEY_RACE_DATE, race.getUnformattedDate());
        values.put(KEY_RACE_URL, race.getUrl());
        return values;
    }

    @AddTrace(name = "sqlite_queryRaces")
    private ArrayList<Race> queryRaces(String query, boolean upcoming) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(query, null);

            return buildRaces(cursor, upcoming);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(db, cursor);
        }
    }

//    public Race getRace(int id) {
//        String query = "SELECT *" +
//                " FROM " + TABLE_RACES +
//                " WHERE " + KEY_RACE_ID + "=" + id;
//
//        ArrayList<Race> races = queryRaces(query);
//        if (races == null || races.isEmpty())
//            return null;
//
//        return races.get(0);
//    }

    /**
     * Retrieves all upcoming races in the database
     *
     * @return list of all races
     */
    public ArrayList<Race> getRaces(boolean favouritesOnly) {
        return getRaces(favouritesOnly, true);
    }


    /**
     * Retrieves all races in the database
     *
     * @param favouritesOnly
     * @param upcoming
     * @return
     */
    public ArrayList<Race> getRaces(boolean favouritesOnly, boolean upcoming) {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("SELECT  r.*, s." + KEY_SERIES_NAME + ",n." + KEY_NOTIFICATIONS_ID + ",n." + KEY_NOTIFICATIONS_DATE_INDEX +
                " FROM " + TABLE_RACES + " r" +
                " LEFT OUTER JOIN " + TABLE_NOTIFICATIONS + " n ON r." + KEY_RACE_ID + "=n." + KEY_NOTIFICATIONS_RACE_ID +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON r." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE r." + KEY_RACE_DATE);

        sBuilder.append(upcoming ? ">=" : "<");
        sBuilder.append("('").append(today).append("')");

        if (favouritesOnly)
            sBuilder.append(" AND s." + KEY_SERIES_FAVOURITE + "=1");

        sBuilder.append(" ORDER BY r." + KEY_RACE_DATE + ",s." + KEY_SERIES_NAME);

        return queryRaces(sBuilder.toString(), upcoming);
    }

    /**
     * Retrieves all upcoming races from the series in the database
     *
     * @param seriesId
     * @return
     */
    public ArrayList<Race> getRaces(int seriesId) {
        return getRaces(seriesId, true);
    }

    /**
     * Retrieves all races from the series in the database
     *
     * @param seriesId
     * @param upcoming
     * @return
     */
    public ArrayList<Race> getRaces(int seriesId, boolean upcoming) {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("SELECT  r.*, s." + KEY_SERIES_NAME + ",n." + KEY_NOTIFICATIONS_ID + ",n." + KEY_NOTIFICATIONS_DATE_INDEX +
                " FROM " + TABLE_RACES + " r" +
                " LEFT OUTER JOIN " + TABLE_NOTIFICATIONS + " n ON r." + KEY_RACE_ID + "=n." + KEY_NOTIFICATIONS_RACE_ID +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON r." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE r." + KEY_RACE_DATE);

        sBuilder.append(upcoming ? ">=" : "<");
        sBuilder.append("('").append(today).append("')");

        sBuilder.append(" AND r." + KEY_RACE_SERIES_ID + "=" + seriesId +
                " ORDER BY r." + KEY_RACE_DATE + ",s." + KEY_SERIES_NAME);

        return queryRaces(sBuilder.toString(), upcoming);
    }


    /**
     * Inserts Races into the database
     *
     * @param list all races to be added
     * @return amount of rows added
     */
    @AddTrace(name = "sqlite_addRaces")
    public int addRaces(ArrayList<Race> list) {
        int totalRowsInserted = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (int i = 0; i < list.size(); i++) {
                Race race = list.get(i);
                if (race == null)
                    continue;

                if (race.getUnformattedDate() == null) {
                    // This race is no longer active. Just remove it from the DB
                    db.delete(TABLE_RACES, KEY_RACE_ID + "=" + race.getId(), null);
                    continue;
                }

                ContentValues cValues = createRaceContentValue(race);
                long rowId = db.insert(TABLE_RACES, null, cValues);
                if (rowId == -1) {
                    cValues.remove(KEY_RACE_ID);
                    rowId = db.update(TABLE_RACES, cValues, KEY_RACE_ID + "=" + race.getId(), null);
                }

                if (rowId != -1)
                    totalRowsInserted++;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeDatabase(db);
        }

        return totalRowsInserted;
    }
    //endregion

    //region Series
    private ArrayList<Series> buildSeries(Cursor cursor) {
        ArrayList<Series> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_SERIES_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_SERIES_NAME));
                int year = cursor.getInt(cursor.getColumnIndex(KEY_SERIES_YEAR));
                boolean favourite = cursor.getInt(cursor.getColumnIndex(KEY_SERIES_FAVOURITE)) == 1;
                String url = cursor.getString(cursor.getColumnIndex(KEY_SERIES_URL));
                String purl = cursor.getString(cursor.getColumnIndex(KEY_SERIES_PURL));


                int totalRaces = 0;
                if (cursor.getColumnCount() > 4)
                    totalRaces = cursor.getInt(4);

                int currRace = 0;
                if (cursor.getColumnCount() > 5)
                    currRace = cursor.getInt(5);


                list.add(new Series(id, name, year, favourite, totalRaces, currRace, url, purl));
            } while (cursor.moveToNext());
        }

        return list;
    }

    private ContentValues createSeriesContentValue(Series series) {
        ContentValues values = new ContentValues();
        values.put(KEY_SERIES_ID, series.getId());
        values.put(KEY_SERIES_NAME, series.getName());
        values.put(KEY_SERIES_YEAR, series.getYear());
        values.put(KEY_SERIES_FAVOURITE, series.isFavorite() ? 1 : 0);
        values.put(KEY_SERIES_URL, series.getUrl());
        values.put(KEY_SERIES_PURL, series.getUrlPrefix());
        return values;
    }

    @AddTrace(name = "sqlite_querySeries")
    private ArrayList<Series> querySeries(String query) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(query, null);

            return buildSeries(cursor);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(db, cursor);
        }
    }

    /**
     * Retrieves all series in the database
     *
     * @return list of series
     */
    public ArrayList<Series> getSeries() {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        String query = "SELECT s." + KEY_SERIES_ID + ",s." + KEY_SERIES_NAME +
                ",s." + KEY_SERIES_YEAR + ",s." + KEY_SERIES_FAVOURITE +
                ",COUNT(DISTINCT r." + KEY_RACE_NUMBER + "), rr." + KEY_RACE_NUMBER +
                ",s." + KEY_SERIES_URL + ",s." + KEY_SERIES_PURL +
                " FROM " + TABLE_SERIES + " s " +
                " LEFT JOIN " + TABLE_RACES + " r ON r." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID +
                " LEFT JOIN " + TABLE_RACES + " rr ON rr." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID + " AND rr." + KEY_RACE_DATE + "<('" + today + "')" +
                " GROUP BY s." + KEY_SERIES_ID +
                " ORDER BY s." + KEY_SERIES_NAME;

        return querySeries(query);
    }

    /**
     * Retrieves a specific series
     *
     * @param id series id
     * @return series
     */
    Series getSeriesWithId(int id) {
        String query = "SELECT  * FROM " + TABLE_SERIES +
                " WHERE " + KEY_SERIES_ID + "=" + id;

        ArrayList<Series> list = querySeries(query);
        if (list != null && list.size() == 1)
            return list.get(0);

        return null;
    }

    /**
     * Inserts Series into the database
     *
     * @param list all series to be added
     * @return amount of rows added
     */
    @AddTrace(name = "sqlite_addSeries")
    public int addSeries(ArrayList<Series> list) {
        // TODO: 14/02/2017 This solution to insert or update is not the best
        int totalRowsInserted = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (int i = 0; i < list.size(); i++) {
                Series series = list.get(i);
                if (series == null)
                    continue;

                ContentValues cValues = createSeriesContentValue(series);
                long rowId = db.insert(TABLE_SERIES, null, cValues);
                if (rowId == -1) {
                    cValues.remove(KEY_SERIES_ID);
                    cValues.remove(KEY_SERIES_FAVOURITE);
                    rowId = db.update(TABLE_SERIES, cValues, KEY_SERIES_ID + "=" + series.getId(), null);
                }

                if (rowId != -1)
                    totalRowsInserted++;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeDatabase(db);
        }

        return totalRowsInserted;
    }

    @AddTrace(name = "sqlite_setSeriesFavorite")
    public void setSeriesFavorite(ObservableArrayList<Series> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (int i = 0; i < list.size(); i++) {
                Series series = list.get(i);
                if (series == null)
                    continue;

                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_SERIES_FAVOURITE, series.isFavorite() ? 1 : 0);

                String whereClause = KEY_SERIES_ID + "=" + series.getId();

                db.update(TABLE_SERIES, contentValues, whereClause, null);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeDatabase(db);
        }
    }
    //endregion

    //region Favourites

    /**
     * @return amount of favourites
     */
    @AddTrace(name = "sqlite_getFavouritesCount")
    public int getFavouritesCount() {
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            return (int) DatabaseUtils.queryNumEntries(db, TABLE_SERIES, KEY_SERIES_FAVOURITE + "=1");
        } finally {
            closeDatabase(db);
        }
    }
    //endregion

    //region NotificationsActivity
    private ArrayList<RCNotification> buildNotifications(Cursor cursor) {
        ArrayList<RCNotification> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_ID));
                int raceId = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_RACE_ID));
                int seriesId = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_SERIES_ID));
                String time = cursor.getString(cursor.getColumnIndex(KEY_NOTIFICATIONS_TIME));
                int minutesBefore = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_MINUTES_BEFORE));
                int timeIndex = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_DATE_INDEX));
                int complete = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_COMPLETED));

                String seriesName = "";
                int seriesNamePos = cursor.getColumnIndex(KEY_SERIES_NAME);
                if (seriesNamePos != -1)
                    seriesName = cursor.getString(seriesNamePos);

                list.add(new RCNotification(id, raceId, seriesId, time, timeIndex, minutesBefore, complete, seriesName));
            } while (cursor.moveToNext());
        }

        return list;
    }

    private ContentValues createNotificationContentValue(RCNotification notification) {
        if (notification == null)
            return null;

        ContentValues values = new ContentValues();
        if (notification.id > 0) {
            values.put(KEY_NOTIFICATIONS_ID, notification.id);
        }

        values.put(KEY_NOTIFICATIONS_RACE_ID, notification.raceId);
        values.put(KEY_NOTIFICATIONS_SERIES_ID, notification.seriesId);
        values.put(KEY_NOTIFICATIONS_TIME, notification.time);
        values.put(KEY_NOTIFICATIONS_DATE_INDEX, notification.timeIndex);

        if (!notification.time.contains("T"))
            notification.minutesBefore = 0;

        values.put(KEY_NOTIFICATIONS_MINUTES_BEFORE, notification.minutesBefore);
        values.put(KEY_NOTIFICATIONS_COMPLETED, notification.complete ? 1 : 0);
        return values;
    }

    @AddTrace(name = "sqlite_queryNotifications")
    private ArrayList<RCNotification> queryNotifications(String query) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(query, null);

            return buildNotifications(cursor);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(db, cursor);
        }
    }

    private long addNotification(RCNotification notification, SQLiteDatabase db) {
        if (notification == null || db == null)
            return -1;

        ContentValues cValues = createNotificationContentValue(notification);
        long rowId = db.insert(TABLE_NOTIFICATIONS, null, cValues);
        if (rowId == -1) {
            cValues.remove(KEY_NOTIFICATIONS_ID);
            rowId = db.update(TABLE_NOTIFICATIONS, cValues, KEY_NOTIFICATIONS_ID + "=" + notification.id, null);
        }

        return rowId;
    }

    /**
     * Add a single notification into the database
     *
     * @param notification Notification to add
     * @return id of the added row, -1 if not successful
     */
    @AddTrace(name = "sqlite_addNotification")
    public long addNotification(RCNotification notification) {
        if (notification == null)
            return -1;

        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            return addNotification(notification, db);
        } finally {
            if (db != null) {
                closeDatabase(db);
            }
        }
    }

    /**
     * Add notifications into the database
     *
     * @param list NotificationsActivity to add
     * @return amount of NotificationsActivity added
     */
    @AddTrace(name = "sqlite_addNotifications")
    public int addNotifications(ArrayList<RCNotification> list) {
        if (list == null || list.size() == 0)
            return -1;

        int totalRowsInserted = 0;
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            for (RCNotification notification : list) {
                if (addNotification(notification, db) != -1)
                    totalRowsInserted++;
            }

            db.setTransactionSuccessful();
        } finally {
            if (db != null) {
                db.endTransaction();
                closeDatabase(db);
            }
        }

        return totalRowsInserted;
    }

    public void setNotificationCompleted(RCNotification notification) {
        if (notification == null)
            return;

        notification.complete = true;
        addNotification(notification);
    }

    /**
     * Retrieve all notifications in the database
     *
     * @return list of notifications
     */
    public ArrayList<RCNotification> getUpcomingNotifications() {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        String query = "SELECT n.*,s." + KEY_SERIES_NAME +
                " FROM " + TABLE_NOTIFICATIONS + " n " +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON n." + KEY_NOTIFICATIONS_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE n." + KEY_NOTIFICATIONS_TIME + ">=('" + today + "')" +
                " ORDER BY " + KEY_NOTIFICATIONS_TIME;

        return queryNotifications(query);
    }

    public RCNotification getNotificationForEvent(long eventId, int index) {
        String query = "SELECT *" +
                " FROM " + TABLE_NOTIFICATIONS +
                " WHERE " + KEY_NOTIFICATIONS_RACE_ID + "=" + eventId +
                " AND " + KEY_NOTIFICATIONS_DATE_INDEX + "=" + index;

        ArrayList<RCNotification> notifications = queryNotifications(query);
        if (notifications == null || notifications.isEmpty())
            return null;

        return notifications.get(0);
    }

    public RCNotification getNotification(long id) {
        String query = "SELECT n.*, s." + KEY_SERIES_NAME +
                " FROM " + TABLE_NOTIFICATIONS + " n " +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON n." + KEY_NOTIFICATIONS_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE " + KEY_NOTIFICATIONS_ID + "=" + id;

        ArrayList<RCNotification> notifications = queryNotifications(query);
        if (notifications == null || notifications.isEmpty())
            return null;

        return notifications.get(0);
    }

    public int removeNotification(RCNotification notification) {
        if (notification == null)
            return -1;

        ArrayList<RCNotification> notifications = new ArrayList<>();
        notifications.add(notification);
        return removeNotifications(notifications);
    }


    /**
     * Remove notifications from the database
     *
     * @param notifications List of notifications to remove
     * @return amount of lines removed
     */
    @AddTrace(name = "sqlite_removeNotifications")
    public int removeNotifications(ArrayList<RCNotification> notifications) {
        if (notifications == null || notifications.isEmpty())
            return 0;

        int removed = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (RCNotification notification : notifications) {
                if (notification == null)
                    continue;

                removed += db.delete(TABLE_NOTIFICATIONS, KEY_NOTIFICATIONS_ID + "=" + notification.id, null);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeDatabase(db);
        }

        return removed;
    }

    //endregion
}
