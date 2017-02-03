package com.pedrojtmartins.racingcalendar.Helpers;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Pedro Martins
 * 02/02/2017
 */

public class DateFormatter {

    @SuppressLint("SimpleDateFormat")
    public static String getSimplifiedDate(String date) {
        if (date == null || date.isEmpty() || !date.contains("-"))
            return "";

        if (!date.contains("T"))
            date += "T00:00:00";

        try {
            SimpleDateFormat currFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM");

            return newFormat.format(currFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
