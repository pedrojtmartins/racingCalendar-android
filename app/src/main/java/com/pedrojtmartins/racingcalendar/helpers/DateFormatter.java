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
        } catch (NullPointerException npe) {
            npe.printStackTrace();
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

    public static int getThisYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getThisWeekNumber() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getTotalWeeksThisYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
    }


    public static String getDate(String date) {
        if (date != null) {
            if (date.contains("T"))
                return date.split("T")[0];

            if (date.contains("-"))
                return date;
        }

        return "";
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
        if (date == null || date.isEmpty())
            return "";

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

        boolean dateOnly = !sDate.contains("T");
        String sFormat = dateOnly ? defaultDataOnlyFormat : defaultFormat;

        try {
            SimpleDateFormat format = new SimpleDateFormat(sFormat);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = format.parse(sDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if (dateOnly) {
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            }

            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isToday(String date) {
        Calendar now = Calendar.getInstance();
        Calendar checking = DateFormatter.getCalendar(date);

        return now.get(Calendar.YEAR) == checking.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == checking.get(Calendar.MONTH) &&
                now.get(Calendar.DAY_OF_MONTH) == checking.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isInTheFuture(String date) {
        Calendar now = Calendar.getInstance();
        Calendar checking = DateFormatter.getCalendar(date);

        if (checking == null)
            return false;

        return checking.after(now);
    }

    public static boolean isThisYear(String fullDate) {
        String thisYear = String.valueOf(DateFormatter.getThisYear());
        return fullDate.startsWith(thisYear);
    }

    public static boolean isThisWeek(String fullDate) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 7);

        return !now.after(getCalendar(fullDate));
    }

    public static long getDateInMillis(String date) {
        Calendar calendar = DateFormatter.getCalendar(date);
        return calendar.getTimeInMillis();
    }

    public static String getFormatted(Date date) {
        try {
            SimpleDateFormat currFormat = new SimpleDateFormat(defaultFormat);
            return currFormat.format(date);
        } catch (IllegalArgumentException iae) {
            return "";
        }
    }

    public static String getFormattedNow() {
        Calendar now = Calendar.getInstance();

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat(defaultFormat);
            return currFormat.format(now.getTime());
        } catch (IllegalArgumentException iae) {
            return "";
        }
    }

    public static String getFormattedDateOnlyNow() {
        Calendar now = Calendar.getInstance();

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat(defaultDataOnlyFormat);
            return currFormat.format(now.getTime());
        } catch (IllegalArgumentException iae) {
            return "";
        }
    }

    public static String getNextDateForDayOfWeek(int weeklyDayOfWeek) {
        Calendar now = Calendar.getInstance();

        int currDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        int daysToAdvance = Helper.getDaysMissingToNextDayOfWeek(currDayOfWeek, weeklyDayOfWeek);

        now.add(Calendar.DAY_OF_MONTH, daysToAdvance);

        return getDate(getFormatted(now.getTime()));
    }

    static class Helper {
        static int getDaysMissingToNextDayOfWeek(int currDayOfWeek, int targetDayOfWeek) {
            if (targetDayOfWeek <= currDayOfWeek)
                targetDayOfWeek += 7;

            return targetDayOfWeek - currDayOfWeek;
        }
    }

    public static long hoursUntil(String date, boolean ignoreHms) {
        Calendar cNow = Calendar.getInstance();
        Calendar cDate = DateFormatter.getCalendar(date);

        if (ignoreHms) {
            cNow.set(Calendar.HOUR, 0);
            cNow.set(Calendar.MINUTE, 0);
            cNow.set(Calendar.SECOND, 0);
            cNow.set(Calendar.MILLISECOND, 0);

            cDate.set(Calendar.HOUR, 0);
            cDate.set(Calendar.MINUTE, 0);
            cDate.set(Calendar.SECOND, 0);
            cDate.set(Calendar.MILLISECOND, 0);
        }

        long millisDiff = cDate.getTimeInMillis() - cNow.getTimeInMillis();
        long secsDiff = millisDiff / 1000;
        long hoursDiff = secsDiff / 3600;

        return hoursDiff;
    }
}
