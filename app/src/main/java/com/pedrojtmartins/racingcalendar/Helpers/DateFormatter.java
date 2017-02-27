package com.pedrojtmartins.racingcalendar.Helpers;

import android.annotation.SuppressLint;

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
            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM");

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

            String fDate = startDay;
            if (!startMonth.equals(endMonth)) {
                fDate += " " + startMonth;
            }
            fDate += " - " + endDay + " " + endMonth;

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
    public static String get24Hour(String date) {
        if (date == null || !date.contains("T") || !date.contains(":"))
            return "";

        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat format24 = new SimpleDateFormat("HH:mm");
            TimeZone thisTimeZone =TimeZone.getDefault();
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
    public static String getAmPmHour(String date) {
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(utcFormat.parse(date));

            DateFormat format24 = new SimpleDateFormat("hh:mm a");
            format24.setTimeZone(TimeZone.getDefault());
            return format24.format(calendar);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
