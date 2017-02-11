package com.pedrojtmartins.racingcalendar.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Pedro Martins
 * 11/02/2017
 */

public class DateHelper {
    public static String getDateNow(Calendar cal, String format) {
        if (cal == null || format == null || format.isEmpty())
            return "";

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat(format);
            return currFormat.format(cal.getTime());
        } catch (IllegalArgumentException iae) {
            return "";
        }
    }
}
