package com.pedrojtmartins.racingcalendar.helpers;

import android.content.Context;
import android.text.format.DateFormat;

import com.pedrojtmartins.racingcalendar._settings.Settings;

/**
 * Pedro Martins
 * 04/03/2017
 */

public class SettingsHelper {
    public static void detectSystemSettings(Context context) {
        //// TODO: 05/03/2017 MAKE THIS PERSISTENT IN CASE THE USER CHANGES IT IN THE SETTINGS
        Settings.IS_12_HOURS_FORMAT = !DateFormat.is24HourFormat(context);
        Settings.DAY_MONTH_FORMAT = DateFormat.getDateFormatOrder(context)[0] == 'M' ? "MMM dd" : "dd MMM";
    }
}