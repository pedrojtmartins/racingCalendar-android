package com.pedrojtmartins.racingcalendar._settings;

/**
 * Pedro Martins
 * 04/03/2017
 */

public class Settings {
    //region Defaults
    public static final boolean NOTIFICATION_REMEMBER = false;
    public static final String NOTIFICATION_MINUTES_BEFORE = "0";

    public static final boolean OPEN_LINK_IN_BROWSER = false;

    public static final boolean IS_MINI_LAYOUT_ALL_ACTIVE = false;
    public static final boolean IS_MINI_LAYOUT_FAV_ACTIVE = false;
    public static final boolean IS_MINI_LAYOUT_SERIES_ACTIVE = false;

    public static final boolean SHOW_OTHER_SERIES_YEARS = false;

    public static final boolean IS_WEEKLY_NOTIFICATION_ACTIVE = true;
    public static final int WEEKLY_NOTIFICATION_DAY_OF_WEEK = 3; //Tuesday (Sunday = 1)
    public static final String WEEKLY_NOTIFICATION_HOUR = "15:00:00";
    //endregion

    public final static String TEST_DEVICE_ID = "B243049824CFD790875E71B3C9DC4847";
    public final static boolean TEST_STORAGE_SERVER = false;

    //region Device configurations
    public static boolean IS_12_HOURS_FORMAT;
    public static String DAY_MONTH_FORMAT;
    //endregion

    public final static String RACE_DATES_DIVIDER = "_";
}