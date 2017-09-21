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
//    public static final boolean IS_MINI_LAYOUT_FAV_ACTIVE = false;
//    public static final boolean IS_MINI_LAYOUT_SERIES_ACTIVE = false;
    //endregion

    public final static String TEST_DEVICE_ID = "B243049824CFD790875E71B3C9DC4847";
    public final static boolean TEST_STORAGE_SERVER = false;

    // BuildConfig.FLAVOR will be used to determine
    // if it is currently running in PRO mode
    //public final static boolean PRO_VERSION = false;

    //region Device configurations
    public static boolean IS_12_HOURS_FORMAT;
    public static String DAY_MONTH_FORMAT;

    public static int SCROLL_ON_TOP_THRESHOLD = 500;
    //endregion

    public final static String RACE_DATES_DIVIDER = "_";
}
