package com.pedrojtmartins.racingcalendar.helpers;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Pedro Martins
 * 11/02/2017
 */
public class DateHelperTest {

    @Test
    public void getDateNow_shouldReturnCorrectDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 0, 2);

        String dateFormat = "yyyy-MM-dd";
        String correctDate = "2017-01-02";

        String returnedDate = DateHelper.getDateNow(cal, dateFormat);

        Assert.assertTrue(returnedDate.equals(correctDate));
    }

    @Test
    public void getDateNow_shouldReturnEmptyIfCalendarIsNull() throws Exception {
        String dateFormat = "yyyy-MM-dd";
        String returnedDate = DateHelper.getDateNow(null, dateFormat);

        Assert.assertTrue(returnedDate.isEmpty());
    }

    @Test
    public void getDateNow_shouldReturnEmptyIfFormatStringIsInvalid() throws Exception {
        String dateFormat = "zxy-po";
        String returnedDate = DateHelper.getDateNow(Calendar.getInstance(), dateFormat);

        Assert.assertTrue(returnedDate.isEmpty());
    }


}