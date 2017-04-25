package com.pedrojtmartins.racingcalendar.helpers;

/**
 * Pedro Martins
 * 25/04/2017
 */

public class ParsingHelper {
    private static final int DEFAULT_INT = 0;

    public static int stringToInt(String sValue, int defaultInt) {
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultInt;
        }
    }

    public static int stringToInt(String sValue) {
        return stringToInt(sValue, DEFAULT_INT);
    }
}
