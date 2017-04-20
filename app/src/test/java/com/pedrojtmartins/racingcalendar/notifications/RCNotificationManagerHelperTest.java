package com.pedrojtmartins.racingcalendar.notifications;

import android.content.res.Resources;

import com.pedrojtmartins.racingcalendar.BuildConfig;
import com.pedrojtmartins.racingcalendar.R;
import com.pedrojtmartins.racingcalendar.models.RCNotification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Pedro Martins
 * 19/04/2017
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RCNotificationManagerHelperTest {
    private Resources resources;

    @Before
    public void setUp() throws Exception {
        resources = Mockito.mock(Resources.class);
        when(resources.getString(R.string.notifRaceBefore)).thenReturn("%s race is starting in %d minutes");
        when(resources.getString(R.string.notifRace)).thenReturn("%s race is starting");
        when(resources.getString(R.string.notifRaceToday)).thenReturn("%s race today");

    }

    //region getNotificationTitle
    @Test
    public void getNotificationTitle_shouldCheckNulls() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, "2017-10-01T10:00:00", 10);
        assertEquals(RCNotificationManager.NotificationManagerHelper.getNotificationTitle(null, "f1", mockedNotifications), "");
        assertEquals(RCNotificationManager.NotificationManagerHelper.getNotificationTitle(resources, null, mockedNotifications), "");
        assertEquals(RCNotificationManager.NotificationManagerHelper.getNotificationTitle(resources, "f1", null), "");
    }

    @Test
    public void getNotificationTitle_shouldReturnTimeBefore() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, "2017-10-01T10:00:00", 10);
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationTitle(resources, "f1", mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("f1 race is starting in 10 minutes"));
    }

    @Test
    public void getNotificationTitle_shouldReturnSetTime() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, "2017-10-01T10:00:00", 0);
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationTitle(resources, "f1", mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("f1 race is starting"));
    }

    @Test
    public void getNotificationTitle_shouldReturnToday() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, "2017-10-01", 20);
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationTitle(resources, "f1", mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("f1 race today"));
    }
    //endregion
}