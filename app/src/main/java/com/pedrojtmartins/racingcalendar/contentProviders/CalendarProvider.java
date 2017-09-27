package com.pedrojtmartins.racingcalendar.contentProviders;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;

import com.pedrojtmartins.racingcalendar._settings.Constants;
import com.pedrojtmartins.racingcalendar.helpers.DateFormatter;
import com.pedrojtmartins.racingcalendar.models.InternalCalendars;
import com.pedrojtmartins.racingcalendar.models.Race;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 25/09/2017
 */

public class CalendarProvider {

    private static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    /**
     * Returns all available calendars
     *
     * @param context activity context
     * @return list of available Calendars
     */
    public static ArrayList<InternalCalendars> getAllCalendars(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR}, Constants.PERMISSION_REQUEST_READ_CALENDAR);
            return null;
        }

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cur = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, EVENT_PROJECTION, null, null, null);
        if (cur == null) {
            return null;
        }

        ArrayList<InternalCalendars> list = new ArrayList<>();
        while (cur.moveToNext()) {
            int calID = cur.getInt(PROJECTION_ID_INDEX);
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            list.add(new InternalCalendars(calID, displayName, accountName, ownerName));
        }

        cur.close();
        return list;
    }

    public static boolean addRaceToCalendar(Activity context, Race race, int calendarId) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_CALENDAR}, Constants.PERMISSION_REQUEST_WRITE_CALENDAR);
            return false;
        }

        for (int i = 0; i < race.getDatesCount(); i++) {

            long startInMillis = DateFormatter.getDateInMillis(race.getFullDate(i));

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
            values.put(CalendarContract.Events.TITLE, race.getSeriesName());
            values.put(CalendarContract.Events.DESCRIPTION, race.getName());
            values.put(CalendarContract.Events.EVENT_LOCATION, race.getLocation());
            values.put(CalendarContract.Events.ALL_DAY, race.hasDateOnly(i));
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");
            values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
            values.put(CalendarContract.Events.DTSTART, startInMillis);

            long endInMillis = startInMillis + (race.getRaceLength() * 1000);
            values.put(CalendarContract.Events.DTEND, endInMillis);


            Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);

        }

        return true;
    }
}
