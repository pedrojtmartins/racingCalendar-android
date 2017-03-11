package com.pedrojtmartins.racingcalendar.Helpers;

import android.annotation.SuppressLint;

import com.pedrojtmartins.racingcalendar._Settings.Settings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Pedro Martins
 * 02/02/2017
 */

public class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    public static String getSimplifiedDate(String date) {
        if (date == null || date.isEmpty() || !date.contains("-"))
            return "";

        if (date.contains("T"))
            date = date.split("T")[0];

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat(Settings.DAY_MONTH_FORMAT);

            return newFormat.format(currFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getWeekInterval(String date) {
        try {
            if (date.contains("T"))
                date = date.split("T")[0];

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(df.parse(date));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            DateFormat dayFormat = new SimpleDateFormat("dd");
            DateFormat monthFormat = new SimpleDateFormat("MMM");
            String startDay = dayFormat.format(calendar.getTime());
            String startMonth = monthFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 6);
            String endDay = dayFormat.format(calendar.getTime());
            String endMonth = monthFormat.format(calendar.getTime());

            String fDate;
            if (Settings.DAY_MONTH_FORMAT.startsWith("M")) {
                if (!startMonth.equals(endMonth)) {
                    fDate = startMonth + " " + startDay + " - " + endMonth + " " + endDay;
                } else {
                    fDate = startMonth + " " + startDay + " - " + endDay;
                }
            } else {
                if (!startMonth.equals(endMonth)) {
                    fDate = startDay + " " + startMonth + " - " + endDay + " " + endMonth;
                } else {
                    fDate = startDay + " - " + endDay + " " + endMonth;
                }
            }

            return fDate;
        } catch (ParseException pe) {
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static int getWeekNumber(String date) {
        try {
            if (date.contains("T"))
                date = date.split("T")[0];

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(df.parse(date));

            return calendar.get(Calendar.WEEK_OF_YEAR);

        } catch (ParseException pe) {
            return 0;
        }
    }

    public static int getThisWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }


    @SuppressLint("SimpleDateFormat")
    private static String get24Hour(String date) {
        if (date == null || !date.contains("T") || !date.contains(":"))
            return "";

        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat format24 = new SimpleDateFormat("HH:mm");
            TimeZone thisTimeZone = TimeZone.getDefault();
            format24.setTimeZone(thisTimeZone);
            Date dDate = calendar.getTime();
            String sDate = format24.format(dDate);
            return sDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static String getAmPmHour(String date) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat format24 = new SimpleDateFormat("h:mm a");
            TimeZone thisTimeZone = TimeZone.getDefault();
            format24.setTimeZone(thisTimeZone);
            Date dDate = calendar.getTime();
            String sDate = format24.format(dDate);
            return sDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getHour(String date) {
        return Settings.IS_12_HOURS_FORMAT ? getAmPmHour(date) : get24Hour(date);
    }

    public static String getDayOfWeekShort(String date) {
        try {
            DateFormat utcFormat = null;
            if (date.contains("T")) {
                utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            } else {
                utcFormat = new SimpleDateFormat("yyyy-MM-dd");
            }

            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat newFormat = new SimpleDateFormat("EEE");
            TimeZone thisTimeZone = TimeZone.getDefault();
            newFormat.setTimeZone(thisTimeZone);
            Date dDate = calendar.getTime();
            String sDate = newFormat.format(dDate);
            return sDate;
        } catch (
                ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
