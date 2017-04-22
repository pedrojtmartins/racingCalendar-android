package com.pedrojtmartins.racingcalendar.helpers;

import android.annotation.SuppressLint;

import com.pedrojtmartins.racingcalendar._settings.Settings;

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

    private static String defaultFormat = "yyyy-MM-dd'T'HH:mm:ss";
    private static String defaultDataOnlyFormat = "yyyy-MM-dd";

    @SuppressLint("SimpleDateFormat")
    public static String getSimplifiedDate(String date) {
        if (date == null || date.isEmpty() || !date.contains("-"))
            return "";

        if (date.contains("T"))
            date = date.split("T")[0];

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat(defaultDataOnlyFormat);
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

            DateFormat df = new SimpleDateFormat(defaultDataOnlyFormat);
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

            DateFormat df = new SimpleDateFormat(defaultDataOnlyFormat);
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
    private static String getHour(String date, String format) {
        if (date == null || !date.contains("T") || !date.contains(":"))
            return "";

        try {
            DateFormat utcFormat = new SimpleDateFormat(defaultFormat);
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat format24 = new SimpleDateFormat(format);
            format24.setTimeZone(TimeZone.getDefault());
            return format24.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getHour(String date) {
        return Settings.IS_12_HOURS_FORMAT ? getHour(date, "h:mm a") : getHour(date, "HH:mm");
    }

    public static String getDayOfWeekShort(String date) {
        try {
            DateFormat utcFormat = null;
            if (date.contains("T")) {
                utcFormat = new SimpleDateFormat(defaultFormat);
            } else {
                utcFormat = new SimpleDateFormat(defaultDataOnlyFormat);
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

    public static Calendar getCalendar(String sDate) {
        if (sDate == null || sDate.isEmpty() || !sDate.contains("-"))
            return null;

        if (!sDate.contains("T")) {
            sDate += "T00:00:00";
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat(defaultFormat);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(sDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
