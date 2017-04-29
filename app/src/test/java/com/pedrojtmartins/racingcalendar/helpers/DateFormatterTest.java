package com.pedrojtmartins.racingcalendar.helpers;

import com.pedrojtmartins.racingcalendar._settings.Settings;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Pedro Martins
 * 02/02/2017
 */
public class DateFormatterTest {

    @Before
    public void setUp() throws Exception {
        Settings.DAY_MONTH_FORMAT = "dd MMM";
    }

    @Test
    public void getSimplifiedDate_ShouldReturnEmptyIfDateIsNull() throws Exception {
        String date = null;
        String simplifiedDate = DateFormatter.getSimplifiedDate(date);
        assertTrue(simplifiedDate.isEmpty());
    }

    @Test
    public void getSimplifiedDate_ShouldReturnCorrectIfDateIsCorrect() throws Exception {
        String date = "2017-01-02T10:20:30";
        String simplifiedDate = DateFormatter.getSimplifiedDate(date);
        assertTrue(simplifiedDate.equals("02 Jan"));
    }

    @Test
    public void getSimplifiedDate_ShouldReturnEmptyIfDateIsIncorrect() throws Exception {
        String date = "10:20:30";
        String simplifiedDate = DateFormatter.getSimplifiedDate(date);
        assertTrue(simplifiedDate.isEmpty());
    }

    @Test
    public void getSimplifiedDate_ShouldReturnCorrectIfDateHasFirstPart() throws Exception {
        String date = "2017-01-02";
        String simplifiedDate = DateFormatter.getSimplifiedDate(date);
        assertTrue(simplifiedDate.equals("02 Jan"));
    }

    @Test
    public void getWeekInterval_shouldReturnCorrectIfDateIsCorrect() throws Exception {
        String date = "2017-01-08";
        String result = DateFormatter.getWeekInterval(date);
        assertTrue(result.equals("02 - 08 Jan"));

        date = "2017-01-09T00:00:00";
        result = DateFormatter.getWeekInterval(date);
        assertTrue(result.equals("09 - 15 Jan"));

        date = "2017-02-01";
        result = DateFormatter.getWeekInterval(date);
        assertTrue(result.equals("30 Jan - 05 Feb"));

        date = "2016-10-09";
        result = DateFormatter.getWeekInterval(date);
        assertTrue(result.equals("03 - 09 Oct"));
    }

    @Test
    public void getWeekInterval_shouldReturnEmptyIfDateIsIncorrect() throws Exception {
        String date = "123";
        String result = DateFormatter.getWeekInterval(date);
        assertTrue(result.equals(""));
    }
}