package com.pedrojtmartins.racingcalendar.Helpers;

import android.content.Context;
import android.text.format.DateFormat;

import com.pedrojtmartins.racingcalendar._Constants.Settings;

/**
 * Pedro Martins
 * 04/03/2017
 */

public class SettingsHelper {
    public static void detectSystemSettings(Context context) {
        Settings.isIn12Hours = !DateFormat.is24HourFormat(context);
    }
}
