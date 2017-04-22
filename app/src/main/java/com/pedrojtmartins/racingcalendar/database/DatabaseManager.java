package com.pedrojtmartins.racingcalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;

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
    private static final int DATABASE_VERSION = 1;
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
    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_RACES = "CREATE TABLE " + TABLE_RACES + "(" +
            KEY_RACE_ID + " INTEGER PRIMARY KEY," +
            KEY_RACE_SERIES_ID + " INTEGER," +
            KEY_RACE_NUMBER + " INTEGER," +
            KEY_RACE_NAME + " TEXT," +
            KEY_RACE_LOCATION + " TEXT," +
            KEY_RACE_DATE + " INTEGER)";
    //endregion
    //endregion

    //region Table Series
    private static final String TABLE_SERIES = "series";

    //region Columns
    private static final String KEY_SERIES_ID = "series_id";
    private static final String KEY_SERIES_NAME = "series_name";
    private static final String KEY_SERIES_YEAR = "series_year";
    private static final String KEY_SERIES_FAVOURITE = "series_favourite";
    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_SERIES = "CREATE TABLE " + TABLE_SERIES + "(" +
            KEY_SERIES_ID + " INTEGER PRIMARY KEY," +
            KEY_SERIES_NAME + " TEXT," +
            KEY_SERIES_YEAR + " INTEGER," +
            KEY_SERIES_FAVOURITE + " INTEGER)";
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

    //endregion

    //region Create Statement
    private static final String CREATE_TABLE_NOTIFICATIONS = "CREATE TABLE " + TABLE_NOTIFICATIONS + "(" +
            KEY_NOTIFICATIONS_ID + " INTEGER PRIMARY KEY," +
            KEY_NOTIFICATIONS_RACE_ID + " INTEGER," +
            KEY_NOTIFICATIONS_SERIES_ID + " INTEGER," +
            KEY_NOTIFICATIONS_TIME + " TEXT," +
            KEY_NOTIFICATIONS_MINUTES_BEFORE + " INTEGER)";
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
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

        onCreate(db);
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
    private ArrayList<Race> buildRaces(Cursor cursor) {
        ArrayList<Race> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_RACE_ID));
                int seriesId = cursor.getInt(cursor.getColumnIndex(KEY_RACE_SERIES_ID));
                int raceNo = cursor.getInt(cursor.getColumnIndex(KEY_RACE_NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(KEY_RACE_NAME));
                String location = cursor.getString(cursor.getColumnIndex(KEY_RACE_LOCATION));
                String date = cursor.getString(cursor.getColumnIndex(KEY_RACE_DATE));

                String seriesName = "";
                int seriesNameId = cursor.getColumnIndex(KEY_SERIES_NAME);
                if (seriesNameId > 0) {
                    seriesName = cursor.getString(cursor.getColumnIndex(KEY_SERIES_NAME));
                    if (seriesName == null)
                        seriesName = "";
                }

                boolean isAlarmSet = cursor.getInt(cursor.getColumnIndex(KEY_NOTIFICATIONS_RACE_ID)) != 0;

                list.add(new Race(id, seriesId, raceNo, name, location, date, seriesName, isAlarmSet));
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
        values.put(KEY_RACE_DATE, race.getFullDate());
        return values;
    }

    private ArrayList<Race> queryRaces(String query) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(query, null);

            return buildRaces(cursor);
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
    public ArrayList<Race> getUpcomingRaces(boolean favouritesOnly) {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("SELECT  r.*, s." + KEY_SERIES_NAME + ",n." + KEY_NOTIFICATIONS_RACE_ID +
                " FROM " + TABLE_RACES + " r" +
                " LEFT OUTER JOIN " + TABLE_NOTIFICATIONS + " n ON r." + KEY_RACE_ID + "=n." + KEY_NOTIFICATIONS_RACE_ID +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON r." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE r." + KEY_RACE_DATE + ">=('").append(today).append("')");

        if (favouritesOnly)
            sBuilder.append(" AND s." + KEY_SERIES_FAVOURITE + "=1");

        sBuilder.append(" ORDER BY r." + KEY_RACE_DATE + ",s." + KEY_SERIES_NAME);

        return queryRaces(sBuilder.toString());
    }

    public ArrayList<Race> getUpcomingRaces(int seriesId) {
        String today = DateHelper.getDateNow(Calendar.getInstance(), "yyyy-MM-dd");
        String query = ("SELECT  r.*, s." + KEY_SERIES_NAME + ",n." + KEY_NOTIFICATIONS_RACE_ID +
                " FROM " + TABLE_RACES + " r" +
                " LEFT OUTER JOIN " + TABLE_NOTIFICATIONS + " n ON r." + KEY_RACE_ID + "=n." + KEY_NOTIFICATIONS_RACE_ID +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON r." + KEY_RACE_SERIES_ID + "=s." + KEY_SERIES_ID +
                " WHERE r." + KEY_RACE_DATE + ">=('" + today + "')" +
                " AND r." + KEY_RACE_SERIES_ID + "=" + seriesId +
                " ORDER BY r." + KEY_RACE_DATE + ",s." + KEY_SERIES_NAME);

        return queryRaces(query);
    }


    /**
     * Inserts Races into the database
     *
     * @param list all races to be added
     * @return amount of rows added
     */
    public int addRaces(ArrayList<Race> list) {
        int totalRowsInserted = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (int i = 0; i < list.size(); i++) {
                Race race = list.get(i);
                if (race == null)
                    continue;

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

                int totalRaces = 0;
                if (cursor.getColumnCount() > 4)
                    totalRaces = cursor.getInt(4);

                int currRace = 0;
                if (cursor.getColumnCount() > 5)
                    currRace = cursor.getInt(5);


                list.add(new Series(id, name, year, favourite, totalRaces, currRace));
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
        return values;
    }

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
        String query = "SELECT s." + KEY_SERIES_ID + ",s." + KEY_SERIES_NAME + ",s." + KEY_SERIES_YEAR + ",s." + KEY_SERIES_FAVOURITE + ",COUNT(DISTINCT r." + KEY_RACE_NUMBER + "), rr." + KEY_RACE_NUMBER +
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

                String seriesName = "";
                if (cursor.getColumnCount() > 5)
                    seriesName = cursor.getString(5);

                list.add(new RCNotification(id, raceId, seriesId, time, minutesBefore, seriesName));
            } while (cursor.moveToNext());
        }

        return list;
    }

    private ContentValues createNotificationContentValue(RCNotification notification) {
        if (notification == null)
            return null;

        ContentValues values = new ContentValues();
        values.put(KEY_NOTIFICATIONS_RACE_ID, notification.raceId);
        values.put(KEY_NOTIFICATIONS_SERIES_ID, notification.seriesId);
        values.put(KEY_NOTIFICATIONS_TIME, notification.time);
        values.put(KEY_NOTIFICATIONS_MINUTES_BEFORE, notification.minutesBefore);
        return values;
    }

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

    /**
     * Add a single notification into the database
     *
     * @param notification Notification to add
     * @return id of the added row, -1 if not successful
     */
    public long addNotification(RCNotification notification) {
        if (notification == null)
            return -1;

        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            ContentValues cValues = createNotificationContentValue(notification);
            long rowId = db.insert(TABLE_NOTIFICATIONS, null, cValues);
            if (rowId == -1) {
                cValues.remove(KEY_NOTIFICATIONS_ID);
                rowId = db.update(TABLE_NOTIFICATIONS, cValues, KEY_NOTIFICATIONS_ID + "=" + notification.id, null);
            }

            return rowId;
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
    public int addNotifications(ArrayList<RCNotification> list) {
        if (list == null || list.size() == 0)
            return 0;

        int totalRowsInserted = 0;
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            for (RCNotification notification : list) {
                if (notification == null)
                    continue;

                ContentValues cValues = createNotificationContentValue(notification);
                if (cValues == null)
                    continue;

                if (db.insert(TABLE_NOTIFICATIONS, null, cValues) != -1)
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

    /**
     * Retrieve all notifications in the database
     *
     * @return list of notifications
     */
    public ArrayList<RCNotification> getNotifications() {
        String query = "SELECT n.*,s." + KEY_SERIES_NAME +
                " FROM " + TABLE_NOTIFICATIONS + " n " +
                " LEFT OUTER JOIN " + TABLE_SERIES + " s ON n." + KEY_NOTIFICATIONS_SERIES_ID + "=s." + KEY_SERIES_ID +
                " ORDER BY " + KEY_NOTIFICATIONS_TIME;

        return queryNotifications(query);

    }

    public RCNotification getNotificationForEvent(long eventId) {
        String query = "SELECT *" +
                " FROM " + TABLE_NOTIFICATIONS +
                " WHERE " + KEY_NOTIFICATIONS_RACE_ID + "=" + eventId;

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
