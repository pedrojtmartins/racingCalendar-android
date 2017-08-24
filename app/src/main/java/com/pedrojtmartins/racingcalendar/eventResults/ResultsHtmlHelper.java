package com.pedrojtmartins.racingcalendar.eventResults;

/**
 * Pedro Martins
 * 18/08/2017
 */

public class ResultsHtmlHelper {


    public static String cleanBefore(String source, String cleanFilter) {
        if (source == null || source.length() == 0)
            return null;

        int index = source.indexOf(cleanFilter);
        if (index == -1)
            return source;

        return source.substring(index + cleanFilter.length());
    }

    public static String cleanAfter(String source, String cleanFilter) {
        if (source == null || source.length() == 0)
            return null;

        int index = source.indexOf(cleanFilter);
        if (index == -1)
            return source;

        return source.substring(0, index);
    }

    public static String getStringAfterValue(String data, String value, String startFilter, String endFilter) {
        int index = data.indexOf(value);
        String nData = data.substring(index);
        return getString(nData, startFilter, endFilter);
    }

    public static String[] splitAndIgnoreFirstPosition(String data, String filter) {
        return splitAndIgnorePositions(data, filter, 1);
    }

    public static String[] splitAndIgnorePositions(String data, String filter, int ignoreCount) {
        if (data == null || filter == null)
            return null;

        String[] split = data.split(filter);
        if (split.length <= 0)
            return null;

        if (ignoreCount >= split.length)
            return null;

        String[] cleanArr = new String[split.length - ignoreCount];
        System.arraycopy(split, ignoreCount, cleanArr, 0, cleanArr.length);

        return cleanArr;
    }

    public static String getString(String source, String startLimiter, String endLimiter) {
        try {
            String[] arr = source.split(startLimiter);
            if (arr.length >= 2) {
                return arr[1].substring(0, arr[1].indexOf(endLimiter));
            }
        } catch (IndexOutOfBoundsException ignored) {

        }

        return "";
    }

    public static String getString(String source, String startLimiter, String endLimiter, int ignoreCount) {
        try {
            String[] arr = source.split(startLimiter);
            if (arr.length > ignoreCount) {
                return arr[ignoreCount + 1].substring(0, arr[ignoreCount + 1].indexOf(endLimiter));
            }
        } catch (IndexOutOfBoundsException ignored) {

        }

        return "";
    }

    public static String getString(String source, String startLimiter) {
        String[] arr = source.split(startLimiter);
        if (arr.length > 1) {
            return arr[1];
        }
        return "";
    }

    public static String getStringUntil(String source, String endLimiter) {
        String[] arr = source.split(endLimiter);
        if (arr.length >= 1) {
            return arr[0];
        }
        return "";
    }
}
