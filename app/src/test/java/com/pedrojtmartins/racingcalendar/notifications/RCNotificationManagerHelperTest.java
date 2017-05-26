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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

        when(resources.getString(R.string.notifRaceBefore)).thenReturn("Green flag in %d minutes");
        when(resources.getString(R.string.notifRace)).thenReturn("Green flag");
        when(resources.getString(R.string.notifRaceToday)).thenReturn("Race is today");

    }

    //region getNotificationMessage
    @Test
    public void getNotificationTitle_shouldCheckNulls() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, 1, "2017-10-01T10:00:00", 0, 10);
        assertEquals(RCNotificationManager.NotificationManagerHelper.getNotificationMessage(null, mockedNotifications), "");
        assertEquals(RCNotificationManager.NotificationManagerHelper.getNotificationMessage(resources, null), "");
    }

    @Test
    public void getNotificationTitle_shouldReturnTimeBefore() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, 1, "2017-10-01T10:00:00", 0, 10);
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationMessage(resources, mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("Green flag in 10 minutes"));
    }

    @Test
    public void getNotificationTitle_shouldReturnSetTime() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, 1, "2017-10-01T10:00:00", 0, 0);
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationMessage(resources, mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("Green flag"));
    }

    @Test
    public void getNotificationTitle_shouldReturnToday() throws Exception {
        RCNotification mockedNotifications = new RCNotification(1, 1, "2017-10-01", 0, 20);
        mockedNotifications.seriesName = "f1";
        String res = RCNotificationManager.NotificationManagerHelper.getNotificationMessage(resources, mockedNotifications);
        assertNotNull(res);
        assertTrue(res.equals("Race is today"));
    }
    //endregion
}