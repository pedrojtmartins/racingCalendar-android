package com.pedrojtmartins.racingcalendar.alarms;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Pedro Martins
 * 18/04/2017
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)

public class RCAlarmManagerTest {

    private Application context;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void setAlarmShouldCheckNulls() throws Exception {
        assertFalse(RCAlarmManager.setAlarm(null, null, null));
        assertFalse(RCAlarmManager.setAlarm(null, Mockito.mock(RCNotification.class), Mockito.mock(PendingIntent.class)));
        assertFalse(RCAlarmManager.setAlarm(Mockito.mock(AlarmManager.class), null, Mockito.mock(PendingIntent.class)));
        assertFalse(RCAlarmManager.setAlarm(Mockito.mock(AlarmManager.class), Mockito.mock(RCNotification.class), null));
    }

    @Test
    public void setAlarmShouldAddAlarms() throws Exception {
        Intent intent = Mockito.mock(Intent.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        RCNotification rcNotification;

        rcNotification = new RCNotification(1, 1, "2017-01-12T00:00:00", 5);
        assertTrue(RCAlarmManager.setAlarm(alarmManager, rcNotification, pendingIntent));

        rcNotification = new RCNotification(1, 2, "2017-01-12", 5);
        assertTrue(RCAlarmManager.setAlarm(alarmManager, rcNotification, pendingIntent));

        rcNotification = new RCNotification(1, 3, "2017-01", 5);
        assertFalse(RCAlarmManager.setAlarm(alarmManager, rcNotification, pendingIntent));
    }

    @Test
    public void removeAlarmShouldCheckNulls() throws Exception {
        assertFalse(RCAlarmManager.removeAlarm(null, null));
        assertFalse(RCAlarmManager.removeAlarm(null, Mockito.mock(PendingIntent.class)));
        assertFalse(RCAlarmManager.removeAlarm(Mockito.mock(AlarmManager.class), null));
    }

    @Test
    public void removeAlarmShouldRemoveAlarm() throws Exception {
        assertTrue(RCAlarmManager.removeAlarm(Mockito.mock(AlarmManager.class), Mockito.mock(PendingIntent.class)));
    }

    @Test
    public void isValidShouldCheckNullsAndEmpties() throws Exception {
        assertEquals(RCAlarmManager.isValid(null), -3);
        assertEquals(RCAlarmManager.isValid(""), -3);
    }

    @Test
    public void isValidShouldReturnTodayWithNoTime() throws Exception {
        Calendar now = Calendar.getInstance();
        String sTodayNoTime = now.get(Calendar.YEAR) + "-" +
                (now.get(Calendar.MONTH) + 1) + "-" +
                now.get(Calendar.DAY_OF_MONTH);

        assertEquals(RCAlarmManager.isValid(sTodayNoTime), -1);
    }

    @Test
    public void isValidShouldReturnInThePast() throws Exception {
        Calendar now = Calendar.getInstance();
        String sTodayNoTime = (now.get(Calendar.YEAR) - 1) + "-" +
                (now.get(Calendar.MONTH) + 1) + "-" +
                now.get(Calendar.DAY_OF_MONTH);

        assertEquals(RCAlarmManager.isValid(sTodayNoTime), -2);

        String sTodayWithTime = sTodayNoTime + "T00:00:00";
        assertEquals(RCAlarmManager.isValid(sTodayWithTime), -2);
    }

    @Test
    public void isValidShouldReturnValid() throws Exception {
        Calendar now = Calendar.getInstance();
        String sTodayNoTime = (now.get(Calendar.YEAR) + 1) + "-" +
                (now.get(Calendar.MONTH) + 1) + "-" +
                now.get(Calendar.DAY_OF_MONTH);

        assertEquals(RCAlarmManager.isValid(sTodayNoTime), 1);

        String sTodayWithTime = sTodayNoTime + "T00:00:00";
        assertEquals(RCAlarmManager.isValid(sTodayWithTime), 1);
    }
}